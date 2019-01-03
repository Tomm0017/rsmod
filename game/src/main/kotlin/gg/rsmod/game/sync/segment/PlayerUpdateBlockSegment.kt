package gg.rsmod.game.sync.segment

import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.game.sync.UpdateBlock
import gg.rsmod.net.packet.DataOrder
import gg.rsmod.net.packet.DataTransformation
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerUpdateBlockSegment(val other: Player, private val newPlayer: Boolean) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        /**
         * TODO(Tom): externalize the structures in which the values are written
         * to the [buf] as they are scrambled on every revision. Could use something
         * like https://developers.google.com/protocol-buffers/ for structures like
         * this. Using the same system as [gg.rsmod.game.message.MessageStructure]
         * could work, but would have to benchmark the performance as [SynchronizationSegment]s
         * are operations that must be handled within a game cycle and are
         * performance-critical.
         */
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
}