package gg.rsmod.game.task

import com.google.common.base.Stopwatch
import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService
import gg.rsmod.game.sync.task.PlayerPostSynchronizationTask
import gg.rsmod.game.sync.task.PlayerPreSynchronizationTask
import gg.rsmod.game.sync.task.PlayerSynchronizationTask
import gg.rsmod.util.NamedThreadFactory
import java.util.concurrent.Executors
import java.util.concurrent.Phaser
import java.util.concurrent.TimeUnit

/**
 * A [GameTask] that is responsible for sending [gg.rsmod.game.model.entity.Pawn]
 * data to [gg.rsmod.game.model.entity.Pawn]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SynchronizationTask(processors: Int) : GameTask {

    /**
     * The [java.util.concurrent.ExecutorService] responsible for the parallel
     * synchronization.
     */
    private val executor = Executors.newFixedThreadPool(processors, NamedThreadFactory().setName("player-sync-thread").build())

    /**
     * The [Phaser] responsible for waiting on every [gg.rsmod.game.model.entity.Player]
     * to finish a stage in the synchronization process before beginning the next stage.
     */
    private val phaser = Phaser(1)

    override fun execute(world: World, service: GameService) {
        var timePhase1 = 0
        var timePhase2 = 0
        var timePhase3 = 0

        val stopwatch = Stopwatch.createUnstarted()
        val worldPlayers = world.players
        val playerCount = worldPlayers.count()

        stopwatch.reset().start()
        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            executor.submit(PlayerPreSynchronizationTask(p, phaser))
        }
        phaser.arriveAndAwaitAdvance()
        timePhase1 = stopwatch.elapsed(TimeUnit.MILLISECONDS).toInt()

        stopwatch.reset().start()
        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            /**
             * Non-human [gg.rsmod.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (p.getType().isHumanControlled() && p.initiated) {
                executor.submit(PlayerSynchronizationTask(p, phaser))
            } else {
                phaser.arriveAndDeregister()
            }
        }
        phaser.arriveAndAwaitAdvance()
        timePhase2 = stopwatch.elapsed(TimeUnit.MILLISECONDS).toInt()

        stopwatch.reset().start()
        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            executor.submit(PlayerPostSynchronizationTask(p, phaser))
        }
        phaser.arriveAndAwaitAdvance()
        timePhase3 = stopwatch.elapsed(TimeUnit.MILLISECONDS).toInt()

        if (timePhase1 + timePhase2 + timePhase3 >= 100) {
            println("Times: phase1=$timePhase1, phase2=$timePhase2, phase3=$timePhase3, processors=${Runtime.getRuntime().availableProcessors()}")
        }
    }

}