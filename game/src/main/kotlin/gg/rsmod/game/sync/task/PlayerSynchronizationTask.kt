package gg.rsmod.game.sync.task

import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.INDEX_ATTR
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.game.sync.SynchronizationTask
import gg.rsmod.game.sync.UpdateBlock
import gg.rsmod.game.sync.segment.*
import gg.rsmod.net.packet.*
import gg.rsmod.util.Misc
import org.apache.logging.log4j.LogManager

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerSynchronizationTask(val player: Player) : SynchronizationTask {

    companion object {
        private val logger = LogManager.getLogger(PlayerSynchronizationTask::class.java)

        private const val MAX_LOCAL_PLAYERS = 255
        private const val MAX_PLAYER_ADDITIONS_PER_CYCLE = 15
    }

    private val nonLocalIndices = arrayListOf<Int>().apply { addAll(1..2047) }

    private fun getSegments(): List<SynchronizationSegment> {
        val segments = arrayListOf<SynchronizationSegment>()

        val localsToRemove = arrayListOf<Player>()
        val nonLocalsToRemove = arrayListOf<Int>()

        var skipCount = 0
        val locals = player.localPlayers

        for (i in 0 until locals.size) {
            val local = locals[i]
            val index = local.attr[INDEX_ATTR]

            nonLocalIndices.remove(index)

            if ((player.otherPlayerSkipFlags[index] and 0x1) != 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                continue
            }

            if (local != player && shouldRemove(local)) {
                player.otherPlayerTiles[index] = 0
                localsToRemove.add(local)
                segments.add(RemoveLocalPlayerSegment())
                continue
            }

            val requiresBlockUpdate = local.blockBuffer.isDirty()
            if (requiresBlockUpdate) {
                segments.add(PlayerUpdateBlockSegment(other = local, newPlayer = false))
            }
            if (local.teleport) {
                segments.add(PlayerTeleportSegment(player = player, other = local,
                        index = index, encodeUpdateBlocks = requiresBlockUpdate))
            } else if (local.steps != null) {
                var dx = Misc.DIRECTION_DELTA_X[local.steps!!.walkDirection!!.getPlayerWalkIndex()]
                var dz = Misc.DIRECTION_DELTA_Z[local.steps!!.walkDirection!!.getPlayerWalkIndex()]
                var running = local.steps!!.runDirection != null

                var direction = 0
                if (running) {
                    dx += Misc.DIRECTION_DELTA_X[local.steps!!.runDirection!!.getPlayerWalkIndex()]
                    dz += Misc.DIRECTION_DELTA_Z[local.steps!!.runDirection!!.getPlayerWalkIndex()]
                    direction = Misc.getPlayerRunningDirection(dx, dz)
                    running = direction != -1
                }
                if (!running) {
                    direction = Misc.getPlayerWalkingDirection(dx, dz)
                }

                segments.add(PlayerWalkSegment(encodeUpdateBlocks = requiresBlockUpdate, running = running,
                        direction = direction))

                if (!requiresBlockUpdate && running) {
                    segments.add(PlayerUpdateBlockSegment(other = local, newPlayer = false))
                }
            } else if (requiresBlockUpdate) {
                segments.add(SignalPlayerUpdateBlockSegment())
            } else {
                for (j in i + 1 until locals.size) {
                    val next = locals[j]
                    val nextIndex = next.attr[INDEX_ATTR]
                    if ((player.otherPlayerSkipFlags[nextIndex] and 0x1) != 0) {
                        continue
                    }
                    if (next.blockBuffer.isDirty() || next.teleport || next.steps != null || next != player && shouldRemove(next)) {
                        break
                    }
                    skipCount++
                }
                segments.add(PlayerSkipCountSegment(skipCount))
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
            }
        }
        locals.removeAll(localsToRemove)

        if (skipCount > 0) {
            throw RuntimeException()
        }

        for (i in 0 until locals.size) {
            val local = locals[i]
            val index = local.attr[INDEX_ATTR]

            if ((player.otherPlayerSkipFlags[index] and 0x1) == 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                continue
            }

            if (local != player && shouldRemove(local)) {
                player.otherPlayerTiles[index] = 0
                localsToRemove.add(local)
                segments.add(RemoveLocalPlayerSegment())
                continue
            }

            val requiresBlockUpdate = local.blockBuffer.isDirty()
            if (requiresBlockUpdate) {
                segments.add(PlayerUpdateBlockSegment(other = local, newPlayer = false))
            }
            if (local.teleport) {
                segments.add(PlayerTeleportSegment(player = player, other = local,
                        index = index, encodeUpdateBlocks = requiresBlockUpdate))
            } else if (local.steps != null) {
                var dx = Misc.DIRECTION_DELTA_X[local.steps!!.walkDirection!!.getPlayerWalkIndex()]
                var dz = Misc.DIRECTION_DELTA_Z[local.steps!!.walkDirection!!.getPlayerWalkIndex()]
                var running = local.steps!!.runDirection != null

                var direction = 0
                if (running) {
                    dx += Misc.DIRECTION_DELTA_X[local.steps!!.runDirection!!.getPlayerWalkIndex()]
                    dz += Misc.DIRECTION_DELTA_Z[local.steps!!.runDirection!!.getPlayerWalkIndex()]
                    direction = Misc.getPlayerRunningDirection(dx, dz)
                    running = direction != -1
                }
                if (!running) {
                    direction = Misc.getPlayerWalkingDirection(dx, dz)
                }

                segments.add(PlayerWalkSegment(encodeUpdateBlocks = requiresBlockUpdate, running = running,
                        direction = direction))

                if (!requiresBlockUpdate && running) {
                    segments.add(PlayerUpdateBlockSegment(other = local, newPlayer = false))
                }
            } else if (requiresBlockUpdate) {
                segments.add(SignalPlayerUpdateBlockSegment())
            } else {
                for (j in i + 1 until locals.size) {
                    val next = locals[j]
                    val nextIndex = next.attr[INDEX_ATTR]
                    if ((player.otherPlayerSkipFlags[nextIndex] and 0x1) == 0) {
                        continue
                    }
                    if (next.blockBuffer.isDirty() || next.teleport || next.steps != null || next != player && shouldRemove(next)) {
                        break
                    }
                    skipCount++
                }
                segments.add(PlayerSkipCountSegment(skipCount))
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
            }
        }
        locals.removeAll(localsToRemove)

        if (skipCount > 0) {
            throw RuntimeException()
        }

        var added = 0
        for (i in 0 until nonLocalIndices.size) {
            val index = nonLocalIndices[i]

            if ((player.otherPlayerSkipFlags[index] and 0x1) == 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                continue
            }

            val nonLocal = if (index < player.world.players.capacity) player.world.players.get(index) else null

            if (nonLocal != null && added < MAX_PLAYER_ADDITIONS_PER_CYCLE
                    && player.localPlayers.size < MAX_LOCAL_PLAYERS && shouldAdd(nonLocal)) {
                val tileHash = nonLocal.tile.to30BitInteger()
                segments.add(AddLocalPlayerSegment(player = player, other = nonLocal, index = index, tileHash = tileHash))
                segments.add(PlayerUpdateBlockSegment(other = nonLocal, newPlayer = true))

                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                player.otherPlayerTiles[index] = tileHash

                player.localPlayers.add(nonLocal)
                player.localPlayers.sortBy { it.index }
                added++
                continue
            }

            for (j in i + 1 until nonLocalIndices.size) {
                val nextIndex = nonLocalIndices[j]
                if ((player.otherPlayerSkipFlags[nextIndex] and 0x1) == 0) {
                    continue
                }
                val next = if (nextIndex < player.world.players.capacity) player.world.players.get(nextIndex) else null
                if (next != null && shouldAdd(next)) {
                    break
                }
                skipCount++
            }
            segments.add(PlayerSkipCountSegment(count = skipCount))
            player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
        }

        if (skipCount > 0) {
            throw RuntimeException()
        }

        nonLocalIndices.removeAll(nonLocalsToRemove)
        for (i in 0 until nonLocalIndices.size) {
            val index = nonLocalIndices[i]

            if ((player.otherPlayerSkipFlags[index] and 0x1) != 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                continue
            }

            val nonLocal = if (index < player.world.players.capacity) player.world.players.get(index) else null

            if (nonLocal != null && added < MAX_PLAYER_ADDITIONS_PER_CYCLE
                    && player.localPlayers.size < MAX_LOCAL_PLAYERS && shouldAdd(nonLocal)) {
                val tileHash = nonLocal.tile.to30BitInteger()
                segments.add(AddLocalPlayerSegment(player = player, other = nonLocal, index = index, tileHash = tileHash))
                segments.add(PlayerUpdateBlockSegment(other = nonLocal, newPlayer = true))

                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                player.otherPlayerTiles[index] = tileHash

                player.localPlayers.add(nonLocal)
                player.localPlayers.sortBy { it.index }
                added++
                continue
            }

            for (j in i + 1 until nonLocalIndices.size) {
                val nextIndex = nonLocalIndices[j]
                if ((player.otherPlayerSkipFlags[nextIndex] and 0x1) != 0) {
                    continue
                }
                val next = if (nextIndex < player.world.players.capacity) player.world.players.get(nextIndex) else null
                if (next != null && shouldAdd(next)) {
                    break
                }
                skipCount++
            }
            segments.add(PlayerSkipCountSegment(count = skipCount))
            player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
        }

        if (skipCount > 0) {
            throw RuntimeException()
        }

        return segments
    }

    override fun run() {
        val buf = GamePacketBuilder(79, PacketType.VARIABLE_SHORT)
        val maskBuf = GamePacketBuilder()

        val segments = getSegments()
        segments.forEach { segment ->
            buf.switchToBitAccess()
            segment.encode(if (segment is PlayerUpdateBlockSegment) maskBuf else buf)
            buf.switchToByteAccess()
        }

        buf.putBytes(maskBuf.getBuffer())
        player.write(buf.toGamePacket())

        for (i in 1 until 2048) {
            player.otherPlayerSkipFlags[i] = player.otherPlayerSkipFlags[i] shr 1
        }
    }

    private fun encodeBlocks(other: Player, buf: GamePacketBuilder, newPlayer: Boolean) {
        var mask = other.blockBuffer.blockValue()

        var forceFacePawn = false
        var forceFaceTile = false

        var forceFace: Tile? = null
        if (newPlayer) {
            mask = mask or other.world.updateBlocks[UpdateBlock.APPEARANCE]!!.playerBit

            if (other.blockBuffer.faceDegrees != 0) {
                mask = mask or other.world.updateBlocks[UpdateBlock.FACE_TILE]!!.playerBit
                forceFaceTile = true
            } else if (other.blockBuffer.facePawnIndex != -1) {
                mask = mask or other.world.updateBlocks[UpdateBlock.FACE_PAWN]!!.playerBit
                forceFacePawn = true
            } else {
                mask = mask or other.world.updateBlocks[UpdateBlock.FACE_TILE]!!.playerBit
                forceFace = other.tile.step(other.lastFacingDirection)
            }
        }

        if (mask >= 0x100) {
            mask = mask or 0x8
            buf.put(DataType.BYTE, mask and 0xFF)
            buf.put(DataType.BYTE, mask shr 8)
        } else {
            buf.put(DataType.BYTE, mask and 0xFF)
        }

        if (other.hasBlock(UpdateBlock.FORCE_CHAT)) {
            buf.putString(other.blockBuffer.forceChat)
        }

        if (other.hasBlock(UpdateBlock.MOVEMENT)) {
            buf.put(DataType.BYTE, DataTransformation.ADD, if (other.teleport) 127 else if (other.steps?.runDirection != null) 2 else 1)
        }

        if (other.hasBlock(UpdateBlock.FACE_TILE) || forceFaceTile || forceFace != null) {
            if (forceFace != null) {
                val srcX = other.tile.x * 64
                val srcZ = other.tile.z * 64
                val dstX = forceFace.x * 64
                val dstZ = forceFace.z * 64
                val degreesX = (srcX - dstX).toDouble()
                val degreesZ = (srcZ - dstZ).toDouble()
                buf.put(DataType.SHORT, DataOrder.LITTLE, (Math.atan2(degreesX, degreesZ) * 325.949).toInt() and 0x7ff)
            } else {
                buf.put(DataType.SHORT, DataOrder.LITTLE, other.blockBuffer.faceDegrees)
            }
        }

        if (other.hasBlock(UpdateBlock.APPEARANCE) || newPlayer) {
            val appBuf = GamePacketBuilder()
            appBuf.put(DataType.BYTE, other.gender.id)
            appBuf.put(DataType.BYTE, other.skullIcon)
            appBuf.put(DataType.BYTE, other.prayerIcon)

            val transmog = other.getTransmogId() >= 0

            if (!transmog) {
                val translation = arrayOf(-1, -1, -1, -1, 2, -1, 3, 5, 0, 4, 6, 1)
                for (i in 0 until 12) {
                    val item = other.equipment[i]
                    if (item != null) {
                        appBuf.put(DataType.SHORT, 0x200 + item.id)
                    } else {
                        if (translation[i] == -1) {
                            appBuf.put(DataType.BYTE, 0)
                        } else {
                            appBuf.put(DataType.SHORT, 0x100 + other.looks[translation[i]])
                        }
                    }
                }
            } else {
                appBuf.put(DataType.SHORT, 0xFFFF)
                appBuf.put(DataType.SHORT, other.getTransmogId())
            }

            for (i in 0 until 5) {
                val color = Math.max(0, other.lookColors[i])
                appBuf.put(DataType.BYTE, color)
            }

            if (!transmog) {
                appBuf.put(DataType.SHORT, 809)
                appBuf.put(DataType.SHORT, 823)
                appBuf.put(DataType.SHORT, 819)
                appBuf.put(DataType.SHORT, 820)
                appBuf.put(DataType.SHORT, 821)
                appBuf.put(DataType.SHORT, 822)
                appBuf.put(DataType.SHORT, 824)
            } else {
                val def = other.world.definitions.get(NpcDef::class.java, other.getTransmogId())
                val animations = arrayOf(def.standAnim, def.walkAnim, def.walkAnim, def.render3,
                        def.render4, def.render5, def.walkAnim)

                animations.forEach { anim ->
                    appBuf.put(DataType.SHORT, anim)
                }
            }

            appBuf.putBytes(other.username.toByteArray())
            appBuf.put(DataType.BYTE, 0) // String terminator
            appBuf.put(DataType.BYTE, other.getSkills().combatLevel)
            appBuf.put(DataType.SHORT, 0)
            appBuf.put(DataType.BYTE, 0)

            buf.put(DataType.BYTE, DataTransformation.SUBTRACT, appBuf.getBuffer().readableBytes())
            buf.putBytes(DataTransformation.ADD, appBuf.getBuffer())
        }

        if (other.hasBlock(UpdateBlock.FACE_PAWN) || forceFacePawn) {
            buf.put(DataType.SHORT, DataTransformation.ADD, other.blockBuffer.facePawnIndex)
        }

        if (other.hasBlock(UpdateBlock.ANIMATION)) {
            buf.put(DataType.SHORT, other.blockBuffer.animation)
            buf.put(DataType.BYTE, other.blockBuffer.animationDelay)
        }

        if (other.hasBlock(UpdateBlock.GFX)) {
            buf.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, other.blockBuffer.graphicId)
            buf.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (other.blockBuffer.graphicHeight shl 16) or other.blockBuffer.graphicDelay)
        }
    }

    private fun shouldAdd(other: Player): Boolean = other.tile.isWithinRadius(player.tile, Player.VIEW_DISTANCE) && !player.localPlayers.contains(other) && other != player

    private fun shouldRemove(other: Player): Boolean = !other.isOnline() || !other.tile.isWithinRadius(player.tile, Player.VIEW_DISTANCE)

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
            buf.putBits(2, dh and 0x3)
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
            buf.putBits(5, ((dh and 0x3) shl 3) or (direction and 0x7))
        } else {
            // If we moved further.
            val dx = Math.floor((dstX / 0x1FFF).toDouble()).toInt()
            val dz = Math.floor((dstZ / 0x1FFF).toDouble()).toInt()
            buf.putBits(2, 3)
            buf.putBits(18, (dz and 0xFF) or ((dx and 0xFF) shl 8) or ((dh and 0x3) shl 16))
        }
    }
}