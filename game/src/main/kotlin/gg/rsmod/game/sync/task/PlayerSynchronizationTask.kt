package gg.rsmod.game.sync.task

import gg.rsmod.game.model.INDEX_ATTR
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.UpdateBlock
import gg.rsmod.net.packet.DataTransformation
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder
import gg.rsmod.net.packet.PacketType
import gg.rsmod.util.Misc
import org.apache.logging.log4j.LogManager
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerSynchronizationTask(val player: Player, override val phaser: Phaser) : PhasedSynchronizationTask(phaser) {

    companion object {
        private val logger = LogManager.getLogger(PlayerSynchronizationTask::class.java)
        private const val MAX_LOCAL_PLAYERS = 255
        private const val MAX_PLAYER_ADDITIONS_PER_CYCLE = 15
        private const val VIEWING_DISTANCE = 14
    }

    private val nonLocalIndices = arrayListOf<Int>().apply { addAll(1..2047) }

    override fun execute() {
        val buf = GamePacketBuilder(79, PacketType.VARIABLE_SHORT)
        val maskBuf = GamePacketBuilder()

        encodeLocal(buf, maskBuf)
        encodeNonLocal(buf, maskBuf)

        buf.putBytes(maskBuf.getBuffer())
        player.write(buf.toGamePacket())
    }

    private fun encodeLocal(buf: GamePacketBuilder, maskBuf: GamePacketBuilder) {
        var skip = 0
        var iteratorIndex = 0
        val iterator = player.localPlayers.iterator()

        buf.switchToBitAccess()
        while (iterator.hasNext()) {
            iteratorIndex++
            val local = iterator.next()
            val index = local.attr[INDEX_ATTR]
            nonLocalIndices.remove(index)

            if (skip > 0) {
                skip--
                continue
            }
            /**
             * Remove player
             */
            if (local != player && shouldRemove(local)) {
                player.otherPlayerTiles[index] = 0
                iterator.remove()

                buf.putBits(1, 1)
                buf.putBits(1, 0)
                buf.putBits(2, 0)
                buf.putBits(1, 0)
                continue
            }

            /**
             * Update player.
             */
            val requiresBlockUpdate = local.blockBuffer.isDirty()
            if (requiresBlockUpdate) {
                encodeBlocks(other = local, maskBuf = maskBuf, newPlayer = false)
            }
            if (local.teleport) {
                buf.putBits(1, 1) // Do not skip this player
                buf.putBit(requiresBlockUpdate) // Does the local player require block update?
                buf.putBits(2, 3) // Teleport movement type

                val lastX = player.otherPlayerTiles[index] shr 14 and 0x3FFF
                val lastZ = player.otherPlayerTiles[index] and 0x3FFF
                val lastH = player.otherPlayerTiles[index] shr 28 and 0x3

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
            } else if (local.steps != null) {
                var dx = Misc.DIRECTION_DELTA_X[local.steps!!.walkDirection!!.getPlayerWalkIndex()]
                var dz = Misc.DIRECTION_DELTA_Z[local.steps!!.walkDirection!!.getPlayerWalkIndex()]
                var running = local.steps!!.runDirection != null

                var movement = 0
                if (running) {
                    dx += Misc.DIRECTION_DELTA_X[local.steps!!.runDirection!!.getPlayerWalkIndex()]
                    dz += Misc.DIRECTION_DELTA_Z[local.steps!!.runDirection!!.getPlayerWalkIndex()]
                    movement = Misc.getPlayerRunningDirection(dx, dz)
                    running = movement != -1
                }
                if (!running) {
                    movement = Misc.getPlayerWalkingDirection(dx, dz)
                }
                buf.putBits(1, 1) // Requires client decoding
                buf.putBits(1, if (requiresBlockUpdate) 1 else 0) // Whether client should decode update masks
                buf.putBits(2, if (running) 2 else 1) // Whether client should decode a walk or a run movement
                buf.putBits(if (running) 4 else 3, movement)

                if (!requiresBlockUpdate && running) {
                    encodeBlocks(local, maskBuf, false)
                }
            } else if (requiresBlockUpdate) {
                buf.putBits(1, 1)
                buf.putBits(1, 1)
                buf.putBits(2, 0)
            } else {
                buf.putBits(1, 0)
                for (i in iteratorIndex until player.localPlayers.size) {
                    val next = player.localPlayers[i]
                    if (next.blockBuffer.isDirty() || next.teleport || next.steps != null || next != player && shouldRemove(next)) {
                        break
                    }
                    skip++
                }
                writeSkip(buf, skip)
            }
        }
        buf.switchToByteAccess()
    }

    private fun encodeNonLocal(buf: GamePacketBuilder, maskBuf: GamePacketBuilder) {
        var skip = 0
        var added = 0

        buf.switchToBitAccess()
        for (i in 0 until nonLocalIndices.size) {
            if (skip > 0) {
                skip--
                continue
            }
            val index = nonLocalIndices[i]
            val nonLocal = if (index < player.world.players.capacity) player.world.players.get(index) else null

            /**
             * Add non local player as local.
             */
            if (nonLocal != null && added < MAX_PLAYER_ADDITIONS_PER_CYCLE
                    && player.localPlayers.size < MAX_LOCAL_PLAYERS && shouldAdd(nonLocal)) {
                player.localPlayers.add(nonLocal)
                player.localPlayers.sortBy { it.index }
                added++

                val tileHash = nonLocal.tile.to30BitInteger()
                buf.putBits(1, 1) // Do not skip this player
                buf.putBits(2, 0) // Require addition to local players

                buf.putBits(1, 1) // Require location hash change
                updateLocation(buf, player.otherPlayerTiles[index], tileHash)
                player.otherPlayerTiles[index] = tileHash

                buf.putBits(13, nonLocal.tile.x and 0x1FFF)
                buf.putBits(13, nonLocal.tile.z and 0x1FFF)
                buf.putBits(1, 1) // Requires block update
                encodeBlocks(other = nonLocal, maskBuf = maskBuf, newPlayer = true)
                continue
            }

            buf.putBits(1, 0)
            for (j in i + 1 until nonLocalIndices.size) {
                val nextIndex = nonLocalIndices[j]
                val next = if (nextIndex < player.world.players.capacity) player.world.players.get(nextIndex) else null
                if (next != null && shouldAdd(next)) {
                    break
                }
                skip++
            }
            writeSkip(buf, skip)
        }
        buf.switchToByteAccess()
    }

    private fun encodeBlocks(other: Player, maskBuf: GamePacketBuilder, newPlayer: Boolean) {
        var mask = other.blockBuffer.blockValue()

        if (newPlayer) {
            mask = mask or UpdateBlock.APPEARANCE.value
        }

        if (mask >= 0x100) {
            mask = mask or 0x20
            maskBuf.put(DataType.BYTE, mask and 0xFF)
            maskBuf.put(DataType.BYTE, mask shr 8)
        } else {
            maskBuf.put(DataType.BYTE, mask and 0xFF)
        }

        if (other.blockBuffer.hasBlock(UpdateBlock.FORCE_CHAT)) {
            maskBuf.putString(other.blockBuffer.getForceChat())
        }

        if (other.blockBuffer.hasBlock(UpdateBlock.MOVEMENT)) {
            maskBuf.put(DataType.BYTE, DataTransformation.NEGATE, if (other.teleport) 127 else if (other.steps?.runDirection != null) 2 else 1)
        }

        if (other.blockBuffer.hasBlock(UpdateBlock.APPEARANCE) || newPlayer) {
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

    private fun shouldAdd(other: Player): Boolean = other.tile.isWithinRadius(player.tile, VIEWING_DISTANCE)// && nearbyPlayers.contains(other)

    private fun shouldRemove(other: Player): Boolean = !other.isOnline() || !other.tile.isWithinRadius(player.tile, VIEWING_DISTANCE)// || !nearbyPlayers.contains(other)

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