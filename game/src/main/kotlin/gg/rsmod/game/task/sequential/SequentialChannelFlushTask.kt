package gg.rsmod.game.task.sequential

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService
import gg.rsmod.game.task.GameTask

/**
 * A [GameTask] responsible for signalling [gg.rsmod.game.model.entity.Player]s
 * that their connection channels must write all data, a.k.a flush, sequentially.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialChannelFlushTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.players.forEach { p ->
            p.channelFlush()
        }
    }
}