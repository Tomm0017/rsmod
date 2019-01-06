package gg.rsmod.game.sync.segment

import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.service.game.ItemStatsService
import gg.rsmod.game.service.game.WeaponRenderService
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.game.sync.UpdateBlockType
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerUpdateBlockSegment(val other: Player, private val newPlayer: Boolean) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        var mask = other.blockBuffer.blockValue()
        val blocks = other.world.playerUpdateBlocks

        var forceFacePawn = false
        var forceFaceTile = false

        var forceFace: Tile? = null
        if (newPlayer) {
            mask = mask or blocks.updateBlocks[UpdateBlockType.APPEARANCE]!!.bit

            if (other.blockBuffer.faceDegrees != 0) {
                mask = mask or blocks.updateBlocks[UpdateBlockType.FACE_TILE]!!.bit
                forceFaceTile = true
            } else if (other.blockBuffer.facePawnIndex != -1) {
                mask = mask or blocks.updateBlocks[UpdateBlockType.FACE_PAWN]!!.bit
                forceFacePawn = true
            } else {
                mask = mask or blocks.updateBlocks[UpdateBlockType.FACE_TILE]!!.bit
                forceFace = other.tile.step(other.lastFacingDirection)
            }
        }

        if (mask >= 0x100) {
            mask = mask or blocks.updateBlockExcessMask
            buf.put(DataType.BYTE, mask and 0xFF)
            buf.put(DataType.BYTE, mask shr 8)
        } else {
            buf.put(DataType.BYTE, mask and 0xFF)
        }

        blocks.updateBlockOrder.forEach { blockType ->
            val force = when (blockType) {
                UpdateBlockType.FACE_TILE -> forceFaceTile || forceFace != null
                UpdateBlockType.FACE_PAWN -> forceFacePawn
                UpdateBlockType.APPEARANCE -> newPlayer
                else -> false
            }
            if (other.hasBlock(blockType) || force) {
                write(buf, blockType, forceFace)
            }
        }
    }

    private fun write(buf: GamePacketBuilder, blockType: UpdateBlockType, forceFace: Tile?) {
        val blocks = other.world.playerUpdateBlocks

        when (blockType) {

            UpdateBlockType.FORCE_CHAT -> {
                // NOTE(Tom): do not need the structure since this value is always
                // written as a string.
                buf.putString(other.blockBuffer.forceChat)
            }

            UpdateBlockType.MOVEMENT -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(structure[0].type, structure[0].order, structure[0].transformation,
                        if (other.teleport) 127 else if (other.steps?.runDirection != null) 2 else 1)
            }

            UpdateBlockType.FACE_TILE -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                if (forceFace != null) {
                    val srcX = other.tile.x * 64
                    val srcZ = other.tile.z * 64
                    val dstX = forceFace.x * 64
                    val dstZ = forceFace.z * 64
                    val degreesX = (srcX - dstX).toDouble()
                    val degreesZ = (srcZ - dstZ).toDouble()
                    buf.put(structure[0].type, structure[0].order, structure[0].transformation, (Math.atan2(degreesX, degreesZ) * 325.949).toInt() and 0x7ff)
                } else {
                    buf.put(structure[0].type, structure[0].order, structure[0].transformation, other.blockBuffer.faceDegrees)
                }
            }

            UpdateBlockType.APPEARANCE -> {
                val appBuf = GamePacketBuilder()
                appBuf.put(DataType.BYTE, other.gender.id)
                appBuf.put(DataType.BYTE, other.skullIcon)
                appBuf.put(DataType.BYTE, other.prayerIcon)

                val transmog = other.getTransmogId() >= 0

                if (!transmog) {
                    val statsService = other.world.getService(ItemStatsService::class.java, false).orElse(null)
                    val translation = arrayOf(-1, -1, -1, -1, 2, -1, 3, 5, 0, 4, 6, 1)

                    val arms = 6
                    val hair = 8
                    val beard = 11

                    for (i in 0 until 12) {
                        if (statsService != null) {
                            if (i == arms) {
                                val item = other.equipment[4]
                                if (item != null) {
                                    if (statsService.get(item.id)?.equipType == arms) {
                                        appBuf.put(DataType.BYTE, 0)
                                        continue
                                    }
                                }
                            } else if (i == hair) {
                                val item = other.equipment[0]
                                if (item != null) {
                                    val equipType = statsService.get(item.id)?.equipType ?: -1
                                    if (equipType == hair || equipType == beard) {
                                        appBuf.put(DataType.BYTE, 0)
                                        continue
                                    }
                                }
                            }
                        }
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
                    val animations = intArrayOf(809, 823, 819, 820, 821, 822, 824)

                    val weapon = other.equipment[3] // Assume slot 3 is the weapon.
                    if (weapon != null) {
                        val renderService = other.world.getService(WeaponRenderService::class.java, searchSubclasses = false).orElse(null)
                        if (renderService != null) {
                            renderService.get(weapon.id)?.animations?.forEachIndexed { index, anim ->
                                animations[index] = anim
                            }
                        }
                    }

                    animations.forEach { anim ->
                        appBuf.put(DataType.SHORT, anim)
                    }
                } else {
                    val def = other.world.definitions.get(NpcDef::class.java, other.getTransmogId())
                    val animations = arrayOf(def.standAnim, def.walkAnim, def.walkAnim, def.render3,
                            def.render4, def.render5, def.walkAnim)

                    animations.forEach { anim ->
                        appBuf.put(DataType.SHORT, anim)
                    }
                }

                appBuf.putString(other.username)
                appBuf.put(DataType.BYTE, other.getSkills().combatLevel)
                appBuf.put(DataType.SHORT, 0)
                appBuf.put(DataType.BYTE, 0)

                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(structure[0].type, structure[0].order, structure[0].transformation, appBuf.getBuffer().readableBytes())
                buf.putBytes(structure[1].transformation, appBuf.getBuffer())
            }

            UpdateBlockType.FACE_PAWN -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(structure[0].type, structure[0].order, structure[0].transformation,
                        other.blockBuffer.facePawnIndex)
            }

            UpdateBlockType.ANIMATION -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(structure[0].type, structure[0].order, structure[0].transformation, other.blockBuffer.animation)
                buf.put(structure[1].type, structure[1].order, structure[1].transformation, other.blockBuffer.animationDelay)
            }

            UpdateBlockType.GFX -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(structure[0].type, structure[0].order, structure[0].transformation, other.blockBuffer.graphicId)
                buf.put(structure[1].type, structure[1].order, structure[1].transformation, (other.blockBuffer.graphicHeight shl 16) or other.blockBuffer.graphicDelay)
            }

            else -> throw RuntimeException("Unhandled update block type: $blockType")
        }
    }
}