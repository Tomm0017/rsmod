package gg.rsmod.game.task

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService

/**
 * A [GameTask] responsible for creating any non-existent [gg.rsmod.game.model.region.Chunk]
 * that players are standing on.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ChunkCreationTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.players.forEach { p ->
            world.chunks.createIfNeeded(p.tile)
        }
    }
}