package gg.rsmod.game.task.parallel

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.service.GameService
import gg.rsmod.game.sync.SynchronizationTask
import gg.rsmod.game.sync.task.*
import gg.rsmod.game.task.GameTask
import mu.KLogging
import java.util.concurrent.ExecutorService
import java.util.concurrent.Phaser

/**
 * A [GameTask] that is responsible for sending [gg.rsmod.game.model.entity.Pawn]
 * data to [gg.rsmod.game.model.entity.Pawn]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ParallelSynchronizationTask(private val executor: ExecutorService) : GameTask {

    /**
     * The [Phaser] responsible for waiting on every [gg.rsmod.game.model.entity.Player]
     * to finish a stage in the synchronization process before beginning the next stage.
     */
    private val phaser = Phaser(1)

    override fun execute(world: World, service: GameService) {
        val worldPlayers = world.players
        val playerCount = worldPlayers.count()
        val worldNpcs = world.npcs
        val rawNpcs = world.npcs.entries
        val npcCount = worldNpcs.count()

        val npcSync = NpcSynchronizationTask(rawNpcs)

        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            submit(phaser, executor, p, PlayerPreSynchronizationTask)
        }
        phaser.arriveAndAwaitAdvance()

        phaser.bulkRegister(npcCount)
        worldNpcs.forEach { n ->
            submit(phaser, executor, n, NpcPreSynchronizationTask)
        }
        phaser.arriveAndAwaitAdvance()

        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            /*
             * Non-human [gg.rsmod.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (p.getType().isHumanControlled() && p.initiated) {
                submit(phaser, executor, p, PlayerSynchronizationTask)
            } else {
                phaser.arriveAndDeregister()
            }
        }
        phaser.arriveAndAwaitAdvance()

        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            /*
             * Non-human [gg.rsmod.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (p.getType().isHumanControlled() && p.initiated) {
                submit(phaser, executor, p, npcSync)
            } else {
                phaser.arriveAndDeregister()
            }
        }
        phaser.arriveAndAwaitAdvance()

        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            submit(phaser, executor, p, PlayerPostSynchronizationTask)
        }
        phaser.arriveAndAwaitAdvance()

        phaser.bulkRegister(npcCount)
        worldNpcs.forEach { n ->
            submit(phaser, executor, n, NpcPostSynchronizationTask)
        }
        phaser.arriveAndAwaitAdvance()
    }

    private fun <T: Pawn> submit(phaser: Phaser, executor: ExecutorService, pawn: T, task: SynchronizationTask<T>) {
        executor.execute {
            try {
                task.run(pawn)
            } catch (e: Exception) {
                logger.error(e) { "Error with task ${this::class.java.simpleName} for $pawn." }
            } finally {
                phaser.arriveAndDeregister()
            }
        }
    }

    companion object: KLogging()
}