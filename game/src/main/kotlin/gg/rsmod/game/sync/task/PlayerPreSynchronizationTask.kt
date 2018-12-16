package gg.rsmod.game.sync.task

import gg.rsmod.game.message.impl.ChangeStaticRegionMessage
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.region.Chunk
import gg.rsmod.game.model.region.ChunkCoords
import gg.rsmod.game.service.GameService
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerPreSynchronizationTask(val player: Player, override val phaser: Phaser) : PhasedSynchronizationTask(phaser) {

    override fun execute() {

        val oldTile = player.lastTile
        player.movementQueue.pulse()
        val newTile = player.tile

        if (player.lastKnownRegionBase == null) {
            val regionX = ((player.tile.x shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3
            val regionZ = ((player.tile.z shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3
            player.lastKnownRegionBase = Tile(regionX, regionZ, player.tile.height)
        }

        val last = player.lastKnownRegionBase!!
        val current = player.tile

        if (shouldUpdateRegion(last, current)) {
            val regionX = ((player.tile.x shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3
            val regionZ = ((player.tile.z shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3

            player.lastKnownRegionBase = Tile(regionX, regionZ, player.tile.height)
            player.write(ChangeStaticRegionMessage(current.x, current.z, player.world.xteaKeyService))
        }


        if (oldTile == null || !oldTile.sameAs(newTile)) {
            var oldSurroundings: MutableSet<ChunkCoords>? = null
            val newSurroundings: MutableSet<ChunkCoords>?

            if (oldTile != null) {
                val oldChunk = player.world.chunks.getForTile(oldTile)
                oldChunk.removeEntity(player.world, player, oldTile)
                oldSurroundings = oldChunk.getSurroundingCoords()
            }

            val newChunk = player.world.chunks.getForTile(newTile)
            newChunk.addEntity(player.world, player, newTile)
            newSurroundings = newChunk.getSurroundingCoords()

            val gameService = player.world.getService(GameService::class.java, false).orElse(null)!!
            if (oldSurroundings != null) {
                newSurroundings.removeAll(oldSurroundings)
            }
            newSurroundings.forEach { coords ->
                val chunk = player.world.chunks.get(coords) ?: return@forEach
                chunk.spawnAll(player, gameService)
            }
        }
    }

    private fun shouldUpdateRegion(old: Tile, new: Tile): Boolean {
        val dx = new.x - old.x
        val dz = new.z - old.z

        return dx <= Player.VIEW_DISTANCE || dz <= Player.VIEW_DISTANCE || dx >= Chunk.MAX_VIEWPORT - Player.VIEW_DISTANCE - 1
                || dz >= Chunk.MAX_VIEWPORT - Player.VIEW_DISTANCE - 1
    }
}