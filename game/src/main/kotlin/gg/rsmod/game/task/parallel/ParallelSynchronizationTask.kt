package gg.rsmod.game.task.parallel

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService
import gg.rsmod.game.sync.task.PhasedSynchronizationTask
import gg.rsmod.game.sync.task.PlayerPostSynchronizationTask
import gg.rsmod.game.sync.task.PlayerPreSynchronizationTask
import gg.rsmod.game.sync.task.PlayerSynchronizationTask
import gg.rsmod.game.task.GameTask
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

        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            executor.submit(PhasedSynchronizationTask(phaser, PlayerPreSynchronizationTask(p)))
        }
        phaser.arriveAndAwaitAdvance()

        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            /**
             * Non-human [gg.rsmod.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (p.getType().isHumanControlled() && p.initiated) {
                executor.submit(PhasedSynchronizationTask(phaser, PlayerSynchronizationTask(p)))
            } else {
                phaser.arriveAndDeregister()
            }
        }
        phaser.arriveAndAwaitAdvance()

        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            executor.submit(PhasedSynchronizationTask(phaser, PlayerPostSynchronizationTask(p)))
        }
        phaser.arriveAndAwaitAdvance()
    }

}