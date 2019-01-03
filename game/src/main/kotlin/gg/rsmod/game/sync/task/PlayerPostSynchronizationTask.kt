package gg.rsmod.game.sync.task

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.service.GameService
import gg.rsmod.game.sync.SynchronizationTask

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerPostSynchronizationTask(val player: Player) : SynchronizationTask {

    override fun run() {
        val oldTile = player.lastTile

        player.teleport = false
        player.lastTile = Tile(player.tile)
        player.steps = null
        player.blockBuffer.clean()

        val oldChunk = if (oldTile != null) player.world.chunks.getOrCreate(oldTile.toChunkCoords(), create = false)!! else null
        val newChunk = player.world.chunks.getOrCreate(player.tile.toChunkCoords(), create = false)
        if (oldChunk != newChunk && newChunk != null) {
            player.world.getService(GameService::class.java, searchSubclasses = false).ifPresent { service ->
                val oldSurroundings = oldChunk?.getSurroundingCoords() ?: hashSetOf()
                val newSurroundings = newChunk.getSurroundingCoords()
                newSurroundings.removeAll(oldSurroundings)

                newSurroundings.forEach { coords ->
                    val chunk = player.world.chunks.getOrCreate(coords, create = false) ?: return@forEach
                    chunk.sendUpdates(player, service)
                }
            }
            if (oldChunk != null) {
                player.world.plugins.executeChunkExit(player, oldChunk.hashCode())
            }
            player.world.plugins.executeChunkEnter(player, newChunk.hashCode())
        }
    }
}