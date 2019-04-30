package gg.rsmod.game.task

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Pawn
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
            p.changeChunks(world, createChunkIfNeeded = true)
        }

        world.npcs.forEach { npc ->
            if (npc.isActive()) {
                npc.changeChunks(world, createChunkIfNeeded = CREATE_CHUNK_FOR_NPC)
            }
        }
    }

    private fun <T : Pawn> T.changeChunks(world: World, createChunkIfNeeded: Boolean) {
        val lastTile = lastChunkTile
        val sameTile = lastTile?.sameAs(tile) ?: false

        if (sameTile) {
            return
        }

        if (lastTile != null) {
            world.chunks.get(lastTile)?.removeEntity(world, this, lastTile)
        }

        world.chunks.get(tile, createIfNeeded = createChunkIfNeeded)?.addEntity(world, this, tile)
        lastChunkTile = Tile(tile)
    }

    companion object {
        /**
         * Flag that specifies if [gg.rsmod.game.model.region.Chunk] should be
         * created if an npc is on it and it doesn't already exist.
         */
        private const val CREATE_CHUNK_FOR_NPC = false
    }
}