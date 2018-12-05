package gg.rsmod.game.task

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService

/**
 * A [GameTask] responsible for signalling [gg.rsmod.game.model.entity.Player]s
 * that their connection channels must write all data, a.k.a flush.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ChannelFlushTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.players.forEach { p ->
            p.channelFlush()
        }
    }
}