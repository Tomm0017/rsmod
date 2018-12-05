package gg.rsmod.game.sync.task

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.block.PlayerUpdateBlock
import gg.rsmod.net.packet.DataTransformation
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder
import gg.rsmod.net.packet.PacketType
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerSynchronizationTask(val player: Player, override val phaser: Phaser) : PhasedSynchronizationTask(phaser) {

    companion object {
        private const val MAX_LOCAL_PLAYERS = 255
        private const val MAX_PLAYER_ADDITIONS_PER_CYCLE = 25
        private const val VIEWING_DISTANCE = 14
    }

    private val nearbyPlayers = arrayListOf<Player>()

    private fun calculateNearbyPlayers() {
        val chunkRadius = when (player.localPlayers.size) {
            in 0..10000 -> 2
            in 0..99 -> 2
            in 100..149 -> 1
            in 150..199 -> 0
            else -> 0
        }
        val surroundingChunks = player.world.maps.getSurroundingChunks(center = player.tile,
                chunkRadius = chunkRadius, activeOnly = true, centerFirst = true, prioritized = true)
        surroundingChunks.forEach { chunk ->
            val localPlayers = player.world.maps.getPlayersInChunk(chunk)
            if (localPlayers != null) {
                nearbyPlayers.addAll(localPlayers.sortedBy { it.index })
            }
        }
        nearbyPlayers.remove(player)
    }

    override fun execute() {
        val packetBuf = GamePacketBuilder(79, PacketType.VARIABLE_SHORT)
        val maskBuf = GamePacketBuilder()

        calculateNearbyPlayers()
        encodeLocal(packetBuf, maskBuf)
        encodeGlobal(packetBuf, maskBuf)

        packetBuf.putBytes(maskBuf.getBuffer())
        player.write(packetBuf.toGamePacket())
    }

    private fun encodeLocal(buf: GamePacketBuilder, maskBuf: GamePacketBuilder) {
        buf.switchToBitAccess()

        val iterator = player.localPlayers.iterator()
        while (iterator.hasNext()) {
            val local = iterator.next()
            if (local == player || !shouldRemove(local)) {
                if (local.teleport) {
                    encodeBlocks(local, maskBuf, true)
                    buf.putBits(1, 1) // Requires client decoding
                    buf.putBits(1, 1) // Block update
                    buf.putBits(2, 3)

                    var dx = local.tile.x
                    var dz = local.tile.z
                    var dh = local.tile.height
                    if (Math.abs(dx) <= VIEWING_DISTANCE && Math.abs(dz) <= VIEWING_DISTANCE) {
                        dx -= (local.lastTile ?: local.tile).x
                        dz -= (local.lastTile ?: local.tile).z
                        dh -= (local.lastTile ?: local.tile).height
                        if (dx < 0) {
                            dx += 32
                        }
                        if (dz < 0) {
                            dz += 32
                        }
                        buf.putBits(1, 0) // Last tile is within view distance
                        buf.putBits(12, dz or (dx shl 5) or (dh shl 10))
                    } else {
                        buf.putBits(1, 1) // Last tile is not within view distance
                        buf.putBits(30, (dz and 0x3FFF) or ((dx and 0x3FFF) shl 14) or ((dh and 0x3) shl 28))
                    }
                } else {
                    val blockUpdate = false
                    if (blockUpdate) {
                        encodeBlocks(local, maskBuf, false)
                    }
                    buf.putBits(1, 0) // Skip player
                    buf.putBits(2, 0) // Don't skip players for now (dev phase)
                }
            } else {
                iterator.remove()
                buf.putBits(1, 1) // Requires client decoding
                buf.putBits(1, 0) // No update block decoding
                buf.putBits(2, 0) // Request player removal
                buf.putBits(1, 0) // No need to decode location hash
            }
        }

        buf.switchToByteAccess()
    }

    private fun encodeGlobal(buf: GamePacketBuilder, maskBuf: GamePacketBuilder) {
        buf.switchToBitAccess()

        var skip = 0
        var added = 0

        // TODO(Tom): global encoding doesn't work properly when a
        // player is removed. Just leaving it like this for now as the sync process
        // on the client we're using is modified and we will need to redo the sync
        // encoding to match a fresh deob anyway.
        val nonLocalPlayers = arrayOfNulls<Player>(2047 - player.localPlayers.size)
        nearbyPlayers.forEach { nearby ->
            if (!player.localPlayers.contains(nearby)) {
                nonLocalPlayers[nearby.index - 1] = nearby
            }
        }

        for (i in 0 until nonLocalPlayers.size) {
            if (skip > 0) {
                --skip
                continue
            }
            val nearbyPlayer = nonLocalPlayers[i]
            if (nearbyPlayer != null && shouldAdd(nearbyPlayer) && added < MAX_PLAYER_ADDITIONS_PER_CYCLE
                    && player.localPlayers.size < MAX_LOCAL_PLAYERS) {
                player.localPlayers.add(nearbyPlayer)
                added++

                encodeBlocks(nearbyPlayer, maskBuf, true)

                buf.putBits(1, 1) // Do not skip this player
                buf.putBits(2, 0) // Signal addition to local players

                buf.putBits(1, 1) // Update position hash
                updateLocation(buf, 0, nearbyPlayer.tile.toInteger())

                buf.putBits(13, nearbyPlayer.tile.x and 0x1FFF)
                buf.putBits(13, nearbyPlayer.tile.z and 0x1FFF)
                buf.putBits(1, 1) // Requires block decoding
            } else {
                buf.putBits(1, 0) // Skip this player
                buf.putBits(2, 0)
            }
        }

        buf.switchToByteAccess()
    }

    private fun encodeBlocks(other: Player, maskBuf: GamePacketBuilder, newPlayer: Boolean) {
        var mask = if (newPlayer) PlayerUpdateBlock.APPEARANCE.getMask() else 0

        if (mask >= 0x100) {
            mask = mask or 0x20
            maskBuf.put(DataType.BYTE, mask and 0xFF)
            maskBuf.put(DataType.BYTE, mask shr 8)
        } else {
            maskBuf.put(DataType.BYTE, mask and 0xFF)
        }

        if ((mask and PlayerUpdateBlock.APPEARANCE.getMask()) != 0) {
            val appBuf = GamePacketBuilder()
            appBuf.put(DataType.BYTE, 0)
            appBuf.put(DataType.BYTE, -1)
            appBuf.put(DataType.BYTE, -1)

            val translation = intArrayOf(-1, -1, -1, -1, 2, -1, 3, 5, 0, 4, 6, 1)
            val looks = intArrayOf(9, 14, 109, 26, 33, 36, 42)
            for (i in 0 until 12) {
                if (translation[i] == -1) {
                    appBuf.put(DataType.BYTE, 0)
                } else {
                    appBuf.put(DataType.SHORT, 0x100 + looks[translation[i]])
                }
            }

            for (i in 0 until 5) {
                appBuf.put(DataType.BYTE, 0)
            }

            appBuf.put(DataType.SHORT, 809)
            appBuf.put(DataType.SHORT, 823)
            appBuf.put(DataType.SHORT, 819)
            appBuf.put(DataType.SHORT, 820)
            appBuf.put(DataType.SHORT, 821)
            appBuf.put(DataType.SHORT, 822)
            appBuf.put(DataType.SHORT, 824)

            appBuf.putBytes(other.username.toByteArray())
            appBuf.put(DataType.BYTE, 0) // String terminator
            appBuf.put(DataType.BYTE, 126)
            appBuf.put(DataType.SHORT, 0)
            appBuf.put(DataType.BYTE, 0)

            appBuf.put(DataType.BYTE, other.privilege.icon)
            appBuf.put(DataType.BYTE, 0)
            appBuf.putBytes("".toByteArray())
            appBuf.put(DataType.BYTE, 0) // String terminator

            maskBuf.put(DataType.BYTE, DataTransformation.NEGATE, appBuf.getBuffer().readableBytes())
            maskBuf.putBytes(appBuf.getBuffer())
        }
    }

    private fun shouldAdd(other: Player): Boolean = player.tile.isWithinRadius(other.tile, VIEWING_DISTANCE)

    private fun shouldRemove(other: Player): Boolean = !player.isOnline() || !player.tile.isWithinRadius(other.tile, VIEWING_DISTANCE) || !nearbyPlayers.contains(other)

    private fun writeSkip(buf: GamePacketBuilder, count: Int) {
        when {
            count == 0 -> {
                buf.putBits(2, 0)
            }
            count < 32 -> {
                buf.putBits(2, 1)
                buf.putBits(5, count)
            }
            count < 256 -> {
                buf.putBits(2, 2)
                buf.putBits(8, count)
            }
            count < 2048 -> {
                buf.putBits(2, 3)
                buf.putBits(11, count)
            }
        }
    }

    private fun updateLocation(buf: GamePacketBuilder, srcHash: Int, dstHash: Int) {
        val srcX = srcHash shr 14 and 0x3FFF
        val srcZ = srcHash and 0x3FFF
        val srcH = srcHash shr 28 and 0x3

        val dstX = dstHash shr 14 and 0x3FFF
        val dstZ = dstHash and 0x3FFF
        val dstH = dstHash shr 28 and 0x3

        val dh = dstH - srcH
        if (srcX == dstX && srcZ == dstZ) {
            // Assume there's only a height difference as we checked
            // that the hashes did not match.
            buf.putBits(2, 1)
            buf.putBits(2, dstH)
        } else if (Math.abs(dstX - srcX) <= 1 && Math.abs(dstZ - srcZ) <= 1) {
            // If we only moved a tile.
            val direction: Int
            val dx = dstX - srcX
            val dy = dstZ - srcZ
            if (dx == -1 && dy == -1)
                direction = 0
            else if (dx == 1 && dy == -1)
                direction = 2
            else if (dx == -1 && dy == 1)
                direction = 5
            else if (dx == 1 && dy == 1)
                direction = 7
            else if (dy == -1)
                direction = 1
            else if (dx == -1)
                direction = 3
            else if (dx == 1)
                direction = 4
            else
                direction = 6
            buf.putBits(2, 2)
            buf.putBits(5, dh shl 3 or (direction and 0x7))
        } else {
            // If we moved further.
            val dx = Math.floor((dstX / 0x1FFF).toDouble()).toInt()
            val dz = Math.floor((dstZ / 0x1FFF).toDouble()).toInt()
            buf.putBits(2, 3)
            buf.putBits(18, dz and 0xff or (dx and 0xff shl 8) or (dh shl 16))
        }
    }
}