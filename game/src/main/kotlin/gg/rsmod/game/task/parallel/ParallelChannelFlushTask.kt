package gg.rsmod.game.task.parallel

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService
import gg.rsmod.game.task.GameTask
import gg.rsmod.util.concurrency.PhasedTask
import java.util.concurrent.ExecutorService
import java.util.concurrent.Phaser

/**
 * A [GameTask] responsible for signalling [gg.rsmod.game.model.entity.Player]s
 * that their connection channels must write all data, a.k.a flush, in parallel.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ParallelChannelFlushTask(private val executor: ExecutorService) : GameTask {

    private val phaser = Phaser(1)

    override fun execute(world: World, service: GameService) {
        val worldPlayers = world.players
        val playerCount = worldPlayers.count()

        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            executor.submit(PhasedTask(phaser, Runnable { p.channelFlush() }))
        }
        phaser.arriveAndAwaitAdvance()
    }

}