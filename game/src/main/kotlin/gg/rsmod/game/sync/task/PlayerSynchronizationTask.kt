package gg.rsmod.game.sync.task

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.UpdateBlock
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
        val chunkRadius = when (player.localPlayerCount) {
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

        encodeLocal(packetBuf, maskBuf, false)
        //encodeLocal(packetBuf, maskBuf, true)
        encodeGlobal(packetBuf, maskBuf, true)
        //encodeGlobal(packetBuf, maskBuf, false)

        packetBuf.putBytes(maskBuf.getBuffer())
        player.write(packetBuf.toGamePacket())

        player.localPlayerCount = 0
        player.nonLocalPlayerCount = 0
        for (i in 1 until 2048) {
            val other = if (i < player.world.players.capacity) player.world.players.get(i) else null
            if (other != null) {
                player.localPlayerIndices[player.localPlayerCount++] = i
            } else {
                player.nonLocalPlayerIndices[player.nonLocalPlayerCount++] = i
            }
        }
    }

    private fun encodeLocal(buf: GamePacketBuilder, maskBuf: GamePacketBuilder, skippedOnly: Boolean) {
        buf.switchToBitAccess()

        var skip = 0
        for (i in 0 until player.localPlayerCount) {
            if (skip > 0) {
                --skip
                continue
            }
            val index = player.localPlayerIndices[i]
            val local = player.world.players.get(index)
            if (local == null || local != player && shouldRemove(local)) {
                buf.putBits(1, 1) // Do not skip this player
                buf.putBits(1, 0) // Does not require block update
                buf.putBits(2, 0) // Player needs to be removed
                buf.putBits(1, 0) // Don't put the player in non-local list
                player.playerTiles[index] = 0 // Reset the local player's tile
                player.localPlayerIndices[i] = 0
                continue
            }
            val dirtyBlocks = local.blockBuffer.isDirty()
            if (dirtyBlocks) {
                encodeBlocks(local, maskBuf, false)
            }
            if (local.teleport) {
                buf.putBits(1, 1) // Do not skip this player
                buf.putBit(dirtyBlocks) // Does the local player require block update?
                buf.putBits(2, 3) // Teleport movement type

                val lastX = player.playerTiles[index] shr 14 and 0x3FFF
                val lastZ = player.playerTiles[index] and 0x3FFF
                val lastH = player.playerTiles[index] shr 28 and 0x3

                var dx = local.tile.x// - lastX
                var dz = local.tile.z// - lastZ
                var dh = local.tile.height// - lastH
                if (Math.abs(dx) <= 14 && Math.abs(dz) <= 14) {
                    dx -= local.lastTile?.x ?: 0
                    dz -= local.lastTile?.z ?: 0
                    dh -= local.lastTile?.height ?: 0
                    if (dx < 0) {
                        dx += 32
                    }
                    if (dz < 0) {
                        dz += 32
                    }
                    buf.putBits(1, 0) // Tiles are within viewing distance
                    buf.putBits(12, (dz) or (dx shl 5) or ((dh and 0x3) shl 10))
                } else {
                    buf.putBits(1, 1) // Tiles aren't within viewing distance
                    buf.putBits(30, (dz and 0x3fff) or ((dx and 0x3fff) shl 14) or ((dh and 0x3) shl 28))
                }
            } else if (local.step != null) {
                buf.putBits(1, 1) // Do not skip this player
            } else if (dirtyBlocks) {
                buf.putBits(1, 1) // Do not skip this player
                buf.putBits(1, 1) // Requires block update
                buf.putBits(2, 0) // Does not require movement update
            } else {
                buf.putBits(1, 0) // Skip this player
                for (j in i + 1 until player.localPlayerCount) {
                    val next = player.world.players.get(player.localPlayerIndices[j])
                    if (next == null || next.blockBuffer.isDirty() || next.teleport || next.step != null || shouldRemove(next)) {
                        break
                    }
                    skip++
                }
                writeSkip(buf, skip)
            }
        }

        buf.switchToByteAccess()
    }

    private fun encodeGlobal(buf: GamePacketBuilder, maskBuf: GamePacketBuilder, skippedOnly: Boolean) {
        buf.switchToBitAccess()

        var added = 0
        var skip = 0

        for (i in 0 until player.nonLocalPlayerCount) {
            if (skip > 0) {
                --skip
                continue
            }
            val index = player.nonLocalPlayerIndices[i]
            val nonLocal = if (index < player.world.players.capacity) player.world.players.get(index) else null
            if (nonLocal != null && shouldAdd(nonLocal) && added++ < MAX_PLAYER_ADDITIONS_PER_CYCLE && player.localPlayerCount < MAX_LOCAL_PLAYERS) {
                val tileHash = nonLocal.tile.toInteger()
                buf.putBits(1, 1) // Do not skip this player
                buf.putBits(2, 0) // Require addition to local players

                buf.putBits(1, 1) // Require location hash change
                updateLocation(buf, player.playerTiles[index], tileHash)
                player.playerTiles[index] = tileHash

                buf.putBits(13, nonLocal.tile.x and 0x1FFF)
                buf.putBits(13, nonLocal.tile.z and 0x1FFF)
                buf.putBits(1, 1) // Requires block update
                encodeBlocks(nonLocal, maskBuf, true)
                player.localPlayerIndices[player.localPlayerCount++] = nonLocal.index
            } else {
                buf.putBits(1, 0) // Skip this player
                for (j in i + 1 until player.nonLocalPlayerCount) {
                    if (player.nonLocalPlayerIndices[j] < player.world.players.capacity) {
                        val next = player.world.players.get(player.nonLocalPlayerIndices[j])
                        if (next != null && shouldAdd(next)) {
                            break
                        }
                    }
                    skip++
                }
                writeSkip(buf, skip)
            }
        }

        buf.switchToByteAccess()
    }

    private fun encodeBlocks(other: Player, maskBuf: GamePacketBuilder, newPlayer: Boolean) {
        var mask = if (newPlayer) UpdateBlock.APPEARANCE.value else other.blockBuffer.blockValue()

        if (mask >= 0x100) {
            mask = mask or 0x20
            maskBuf.put(DataType.BYTE, mask and 0xFF)
            maskBuf.put(DataType.BYTE, mask shr 8)
        } else {
            maskBuf.put(DataType.BYTE, mask and 0xFF)
        }

        if ((mask and 0x1) != 0) {
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
            println("sending app with size: ${appBuf.getBuffer().readableBytes()}")
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