package gg.rsmod.game.task

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService

/**
 * A [GameTask] responsible for creating any non-existent [gg.rsmod.game.model.region.Chunk]
 * that players are standing on as well as registering and de-registering the
 * player from the respective [gg.rsmod.game.model.region.Chunk]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ChunkCreationTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.players.forEach { p ->

            if (p.lastChunkTile == null || !p.tile.sameAs(p.lastChunkTile!!)) {

                if (p.lastChunkTile != null) {
                    val tile = p.lastChunkTile!!
                    val oldChunk = world.chunks.get(tile.asChunkCoords, createIfNeeded = false)
                    oldChunk?.removeEntity(world, p, tile)
                }

                val newChunk = world.chunks.get(p.tile.asChunkCoords, createIfNeeded = true)!!
                newChunk.addEntity(world, p, p.tile)

                p.lastChunkTile = Tile(p.tile)
            }
        }
    }
}