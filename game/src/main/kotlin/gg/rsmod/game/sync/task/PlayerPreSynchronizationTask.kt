package gg.rsmod.game.sync.task

import gg.rsmod.game.message.impl.ChangeStaticRegionMessage
import gg.rsmod.game.model.PlayerGpi
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerPreSynchronizationTask(val player: Player, override val phaser: Phaser) : PhasedSynchronizationTask(phaser) {

    override fun execute() {
        player.movementQueue.pulse()

        if (player.lastKnownRegionBase == null) {
            val regionX = ((player.tile.x shr 3) - 6) * 8
            val regionZ = ((player.tile.z shr 3) - 6) * 8
            player.lastKnownRegionBase = Tile(regionX, regionZ, player.tile.height)
        } else {
            val last = player.lastKnownRegionBase!!
            val current = player.tile

            val dx = current.x - last.x
            val dz = current.z - last.z
            val changedHeight = current.height != last.height

            if (player.getType().isHumanControlled() && (dx <= 15 || dz <= 15 || dx >= 88 || dz >= 88)) {
                val regionX = ((current.x shr 3) - 6) * 8
                val regionZ = ((current.z shr 3) - 6) * 8
                player.lastKnownRegionBase = Tile(regionX, regionZ, current.height)

                val xteaBuf = GamePacketBuilder()
                val chunkX = current.x / 8
                val chunkZ = current.z / 8

                var forceSend = false
                if ((chunkX / 8 == 48 || chunkX / 8 == 49) && chunkZ / 8 == 48) {
                    forceSend = true
                }
                if (chunkX / 8 == 48 && chunkZ / 8 == 148) {
                    forceSend = true
                }

                val lx = (chunkX - (PlayerGpi.MAP_SIZE shr 4)) / 8
                val rx = (chunkX + (PlayerGpi.MAP_SIZE shr 4)) / 8
                val lz = (chunkZ - (PlayerGpi.MAP_SIZE shr 4)) / 8
                val rz = (chunkZ + (PlayerGpi.MAP_SIZE shr 4)) / 8

                var count = 0
                for (x in lx..rx) {
                    for (z in lz..rz) {
                        if (!forceSend || z != 49 && z != 149 && z != 147 && x != 50 && (x != 49 || x != 47)) {
                            val region = z + (x shl 8)
                            val keys = PlayerGpi.xteaKeys!!.get(region)
                            for (key in keys) {
                                xteaBuf.put(DataType.INT, key)
                            }
                        }
                        count++
                    }
                }

                val xteas = ByteArray(xteaBuf.getBuffer().readableBytes())
                xteaBuf.getBuffer().readBytes(xteas)
                player.write(ChangeStaticRegionMessage(chunkX, chunkZ, count, xteas))
            }
        }
    }
}