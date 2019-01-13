package gg.rsmod.game.task

import gg.rsmod.game.model.Tile
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
            if (p.lastChunkTile == null || !p.tile.sameAs(p.lastChunkTile!!)) {

                if (p.lastChunkTile != null) {
                    val tile = p.lastChunkTile!!
                    val oldChunk = world.chunks.getOrCreate(tile.toChunkCoords(), create = false)
                    oldChunk?.removeEntity(world, p, tile)
                }

                val newChunk = world.chunks.getOrCreate(p.tile.toChunkCoords(), create = true)!!
                newChunk.addEntity(world, p, p.tile)

                p.lastChunkTile = Tile(p.tile)
            }
        }
    }
}