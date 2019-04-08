package gg.rsmod.game.sync.task

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.service.GameService
import gg.rsmod.game.sync.SynchronizationTask
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PlayerPostSynchronizationTask : SynchronizationTask<Player> {

    override fun run(pawn: Player) {
        val oldTile = pawn.lastTile
        val moved = oldTile == null || !oldTile.sameAs(pawn.tile)

        if (moved) {
            pawn.lastTile = Tile(pawn.tile)
        }
        pawn.teleport = false
        pawn.steps = null
        pawn.blockBuffer.clean()

        val oldChunk = if (oldTile != null) pawn.world.chunks.get(oldTile.chunkCoords, createIfNeeded = false) else null
        val newChunk = pawn.world.chunks.get(pawn.tile.chunkCoords, createIfNeeded = false)
        if (oldChunk != newChunk && newChunk != null) {
            pawn.world.getService(GameService::class.java)?.let { service ->
                val oldSurroundings = oldChunk?.coords?.getSurroundingCoords() ?: ObjectOpenHashSet()
                val newSurroundings = newChunk.coords.getSurroundingCoords()
                newSurroundings.removeAll(oldSurroundings)

                newSurroundings.forEach { coords ->
                    val chunk = pawn.world.chunks.get(coords, createIfNeeded = false) ?: return@forEach
                    chunk.sendUpdates(pawn, service)
                }
            }
            if (oldChunk != null) {
                pawn.world.plugins.executeChunkExit(pawn, oldChunk.hashCode())
            }
            pawn.world.plugins.executeChunkEnter(pawn, newChunk.hashCode())
        }
    }
}