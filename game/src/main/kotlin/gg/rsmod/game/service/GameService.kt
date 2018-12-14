package gg.rsmod.game.service

import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.game.message.MessageDecoderSet
import gg.rsmod.game.message.MessageEncoderSet
import gg.rsmod.game.message.MessageStructureSet
import gg.rsmod.game.model.World
import gg.rsmod.game.task.*
import gg.rsmod.util.NamedThreadFactory
import gg.rsmod.util.ServerProperties
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.apache.logging.log4j.LogManager
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * The service used to schedule and execute logic needed for the game to run properly.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class GameService : Service() {

    companion object {
        private val logger = LogManager.getLogger(GameService::class.java)!!

        /**
         * The amount of ticks that must go by for debug info to be logged.
         */
        private const val TICKS_PER_DEBUG_LOG = 10
    }

    /**
     * The associated world with our current game.
     */
    lateinit var world: World

    /**
     * The max amount of incoming [gg.rsmod.net.message.Messages]s that can be
     * handled per cycle.
     */
    var maxMessagesPerCycle = 0

    /**
     * The scheduler for our game cycle logic as well as coroutine dispatcher.
     */
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(NamedThreadFactory().setName("game-context").build())

    /**
     * A list of jobs that will be executed on the next cycle after being
     * submitted.
     */
    private val gameThreadJobs = ConcurrentLinkedQueue<Runnable>()

    /**
     * The amount of ticks that have gone by since the last debug log.
     */
    private var debugTick = 0

    /**
     * The total time, in milliseconds, that the past [TICKS_PER_DEBUG_LOG]
     * cycles have taken to complete.
     */
    private var cycleTime = 0

    /**
     * The Kotlin Coroutine dispatcher to submit suspendable plugins.
     */
    val dispatcher: CoroutineDispatcher = executor.asCoroutineDispatcher()

    /**
     * The amount of time, in milliseconds, that each [GameTask] has taken away
     * from the game cycle.
     */
    private val taskTimes = hashMapOf<Class<GameTask>, Long>()

    /**
     * The amount of time, in milliseconds, that [PlayerCycleTask] has taken
     * for each [gg.rsmod.game.model.entity.Player].
     */
    val playerTimes = hashMapOf<String, Long>()

    /**
     * A list of tasks that will be executed per game cycle.
     */
    private val tasks = arrayListOf(
            /**
             * Pre-synchronization tasks.
             */
            MessageHandlerTask(),
            PluginHandlerTask(),
            PlayerCycleTask(),
            MapHandlerTask(),

            /**
             * Synchronization tasks.
             */
            SynchronizationTask(Runtime.getRuntime().availableProcessors() / 2),

            /**
             * Post-synchronization tasks.
             */
            ChannelFlushTask()
    )

    val messageStructures = MessageStructureSet()

    val messageEncoders = MessageEncoderSet()

    val messageDecoders = MessageDecoderSet()

    @Throws(Exception::class)
    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        this.world = world
        maxMessagesPerCycle = serviceProperties.get<Int>("messages-per-cycle")!!
        executor.scheduleAtFixedRate(this::cycle, 0, world.gameContext.cycleTime.toLong(), TimeUnit.MILLISECONDS)
    }

    override fun terminate(server: Server, world: World) {
    }

    /**
     * Submits a job that must be performed on the game-thread.
     */
    fun submitGameThreadJob(job: Runnable) {
        gameThreadJobs.offer(job)
    }

    private fun cycle() {
        val start = System.currentTimeMillis()

        /**
         * Clear the time it has taken to complete [GameTask]s from last cycle.
         */
        taskTimes.clear()
        playerTimes.clear()

        /**
         * Execute any logic jobs that were submitted.
         */
        gameThreadJobs.forEach { job ->
            try {
                job.run()
            } catch (e: Exception) {
                logger.error("Error executing game-thread job.", e)
            }
        }
        /**
         * Reset the logic jobs as they have been completed.
         */
        gameThreadJobs.clear()

        /**
         * Go over the [tasks] and execute their logic. Log the time it took
         * each [GameTask] to complete. Some of the tasks may also calculate
         * their time for each player so that we can have the amount of time,
         * in milliseconds, that each player took to perform certain tasks.
         */
        tasks.forEach { task ->
            val taskStart = System.currentTimeMillis()
            try {
                task.execute(world, this)
            } catch (e: Exception) {
                logger.error("Error with task ${task.javaClass.simpleName}.", e)
            }
            taskTimes[task.javaClass] = System.currentTimeMillis() - taskStart
        }

        /**
         * Increment the world cycle count.
         */
        if (world.currentCycle++ >= Int.MAX_VALUE - 1000) {
            world.currentCycle = 0
            logger.info("World cycle has been reset.")
        }

        /**
         * Calculate the time, in milliseconds, it took for this cycle to complete
         * and add it to [cycleTime].
         */
        cycleTime += (System.currentTimeMillis() - start).toInt()

        if (debugTick++ >= TICKS_PER_DEBUG_LOG) {
            val freeMemory = Runtime.getRuntime().freeMemory()
            val totalMemory = Runtime.getRuntime().totalMemory()
            val maxMemory = Runtime.getRuntime().maxMemory()

            logger.info("[Cycle time avg: {}ms] [Entities: {}p / {}n] [Live plugins: {}] [Mem usage: U={}MB / R={}MB / M={}MB].",
                    cycleTime / TICKS_PER_DEBUG_LOG, world.players.count(), world.getNpcCount(), world.pluginExecutor.getActiveCount(),
                    (totalMemory - freeMemory) / (1024 * 1024), totalMemory / (1024 * 1024), maxMemory / (1024 * 1024))
            debugTick = 0
            cycleTime = 0
        }

        val freeTime = world.gameContext.cycleTime - (System.currentTimeMillis() - start)
        if (freeTime < 0) {
            /**
             * If the cycle took more than [GameContext.cycleTime]ms, we log the
             * occurrence as well as the time each [GameTask] took to complete,
             * as well as how long each [gg.rsmod.game.model.entity.Player] took
             * to process this cycle.
             */
            logger.fatal("Cycle took longer than expected: ${(-freeTime) + world.gameContext.cycleTime}ms / ${world.gameContext.cycleTime}ms!")
            logger.fatal(taskTimes.toList().sortedByDescending { (_, value) -> value }.toMap())
            logger.fatal(playerTimes.toList().sortedByDescending { (_, value) -> value }.toMap())
        }
    }
}