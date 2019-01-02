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
            val oldChunk = if (p.lastTile != null) world.chunks.getOrCreate(p.lastTile!!.toChunkCoords(), create = false) else null
            val newChunk = world.chunks.getOrCreate(p.tile.toChunkCoords(), create = true)!!

            oldChunk?.removeEntity(world, p, p.lastTile!!)
            newChunk.addEntity(world, p, p.tile)
        }
    }
}