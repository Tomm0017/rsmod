package gg.rsmod.game.sync.segment

import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.ChatMessage
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.game.sync.block.UpdateBlockType
import gg.rsmod.net.packet.*

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
            when {
                other.blockBuffer.faceDegrees != 0 -> {
                    mask = mask or blocks.updateBlocks[UpdateBlockType.FACE_TILE]!!.bit
                    forceFaceTile = true
                }

                other.blockBuffer.facePawnIndex != -1 -> {
                    mask = mask or blocks.updateBlocks[UpdateBlockType.FACE_PAWN]!!.bit
                    forceFacePawn = true
                }

                else -> {
                    mask = mask or blocks.updateBlocks[UpdateBlockType.FACE_TILE]!!.bit
                    forceFace = other.tile.step(other.lastFacingDirection)
                }
            }
        }

        val firstExtensionBit = blocks.updateBlockExcessMask8
        val secondExtensionBit = blocks.updateBlockExcessMask16

        if (mask and 0xFF.inv() != 0) {
            mask = mask or firstExtensionBit
        }
        if (mask and 0xFFFF.inv() != 0) {
            mask = mask or secondExtensionBit
        }
        buf.put(DataType.BYTE, mask)
        if (mask and firstExtensionBit != 0) {
            buf.put(DataType.BYTE, mask ushr 8)
        }
        if (mask and secondExtensionBit != 0) {
            buf.put(DataType.BYTE, mask ushr 16)
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


            UpdateBlockType.PUBLIC_CHAT -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                val chatMessage = other.blockBuffer.publicChat
                if (chatMessage.text.length >= 80) return
                val compressed = ByteArray(256)
                compressed[0] = chatMessage.text.length.toByte()
                val compressedLength = other.world.huffman.compress(chatMessage.text, compressed, 1)
                buf.put(structure[0].type, structure[0].order, structure[0].transformation,
                    (chatMessage.color.id shl 8) or chatMessage.effect.id
                )
                buf.put(structure[1].type, structure[1].order, structure[1].transformation, chatMessage.icon)
                buf.put(structure[2].type, structure[2].order, structure[2].transformation, if (chatMessage.type == ChatMessage.ChatType.AUTOCHAT) 1 else 0)
                buf.put(structure[3].type, structure[3].order, structure[3].transformation, compressedLength)
                //buf.putBytes(structure[4].transformation, compressed, 0, compressedLength)
                buf.putBytesReverse(structure[4].transformation, compressed, compressedLength)
            }

            UpdateBlockType.FORCE_CHAT -> {
                // NOTE(Tom): do not need the structure since this value is always
                // written as a string.
                buf.putString(other.blockBuffer.forceChat)
            }

            UpdateBlockType.MOVEMENT -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(
                    structure[0].type, structure[0].order, structure[0].transformation,
                    if (other.blockBuffer.teleport) 127 else if (other.steps?.runDirection != null) 2 else 1
                )
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
                    buf.put(
                        structure[0].type,
                        structure[0].order,
                        structure[0].transformation,
                        (Math.atan2(degreesX, degreesZ) * 325.949).toInt() and 0x7ff
                    )
                } else {
                    buf.put(
                        structure[0].type,
                        structure[0].order,
                        structure[0].transformation,
                        other.blockBuffer.faceDegrees
                    )
                }
            }

            UpdateBlockType.APPEARANCE -> {
                val appBuf = GamePacketBuilder()
                appBuf.put(DataType.BYTE, other.appearance.gender.id)
                appBuf.put(DataType.BYTE, other.skullIcon)
                appBuf.put(DataType.BYTE, other.prayerIcon)
                println("Gender: ${other.appearance.gender.id} Skull: ${other.skullIcon} Prayer: ${other.prayerIcon}")
                val transmog = other.getTransmogId() >= 0
                if (!transmog) {
                    val translation = arrayOf(-1, -1, -1, -1, 2, -1, 3, 5, 0, 4, 6, 1)
                    val skippedSlots = BooleanArray(12)
                    for (i in 0 until 12) {
                        val item = other.equipment[i] ?: continue
                        val itemDef = (Item(item.id)).getDef(other.world.definitions)
                        val wearpos2 = itemDef.wearPos2
                        if (wearpos2 != -1) skippedSlots[wearpos2] = true
                        val wearpos3 = itemDef.wearPos3
                        if (wearpos3 != -1) skippedSlots[wearpos3] = true
                    }
                    for (i in 0 until 12) {
                        if (skippedSlots[i]) {
                            appBuf.put(DataType.BYTE, 0)
                            continue
                        }
                        val item = other.equipment[i]
                        if (item == null) {
                            if (translation[i] == -1) {
                                appBuf.put(DataType.BYTE, 0)
                            } else {
                                val look = other.appearance.getLook(translation[i])
                                if (look == -1) {
                                    appBuf.put(DataType.BYTE, 0)
                                } else {
                                    appBuf.put(DataType.SHORT, 0x100 + look)
                                }
                            }
                        } else {
                            appBuf.put(DataType.SHORT, 0x200 + item.id)
                        }
                    }
                } else {
                    appBuf.put(DataType.SHORT, 0xFFFF)
                    appBuf.put(DataType.SHORT, other.getTransmogId())
                }
                appBuf.put(DataType.BYTE, 20)
                for (i in 0 until 12) {
                    appBuf.put(DataType.BYTE, 0)
                }


                for (i in 0 until 5) {
                    val color = Math.max(0, other.appearance.colors[i])
                    appBuf.put(DataType.BYTE, color)
                }


                when {
                    transmog -> {
                        val def = other.world.definitions.get(NpcDef::class.java, other.getTransmogId())
                        val animations = arrayOf(
                            def.standAnim, def.walkAnim, def.walkAnim, def.rotateLeftAnim,
                            def.rotateRightAnim, def.rotateBackAnim, def.walkAnim
                        )

                        animations.forEach { anim ->
                            appBuf.put(DataType.SHORT, anim)
                        }
                    }

                    other.isAppearimation() -> {
                        other.anims.forEach { anim ->
                            appBuf.put(DataType.SHORT, anim)
                        }
                        other.setAppearimation(false)
                    }

                    else -> {
                        val animations = arrayOf(808, 823, 819, 820, 821, 822, 824)

                        val weapon = other.equipment[3] // Assume slot 3 is the weapon.
                        if (weapon != null) {
                            val def = weapon.getDef(other.world.definitions)
                            def.renderAnimations?.forEachIndexed { index, anim ->
                                var ani = anim
                                if (anim == 0) {
                                    ani = animations[index]
                                }
                                animations[index] = ani
                            }
                        }
                        animations.forEach { anim ->
                            appBuf.put(DataType.SHORT, anim)
                        }
                    }
                }

                appBuf.putString(other.username)
                appBuf.put(DataType.BYTE, other.combatLevel)
                appBuf.put(DataType.SHORT, 0) // skillLevel
                appBuf.put(DataType.BYTE, 0) // isHidden


                appBuf.put(DataType.SHORT, 0)
                appBuf.putString("")
                appBuf.putString("")
                appBuf.putString("")
                appBuf.put(DataType.BYTE, other.appearance.gender.id)
                val structure = blocks.updateBlocks[blockType]!!.values
                println("Readable bytes: ${appBuf.byteBuf.readableBytes()}")
                buf.put(
                    structure[0].type,
                    structure[0].order,
                    structure[0].transformation,
                    appBuf.byteBuf.readableBytes()
                )
                println(appBuf.byteBuf.array().contentToString())
                if (structure[1].order == DataOrder.REVERSED) {
                    val bytes = ByteArray(appBuf.readableBytes)
                    appBuf.byteBuf.readBytes(bytes)
                    buf.putBytes(structure[1].transformation, bytes.reversedArray())
                } else {
                    buf.putBytes(structure[1].transformation, appBuf.byteBuf)
                }

            }

            UpdateBlockType.HITMARK -> {
                val structure = blocks.updateBlocks[blockType]!!.values

                val hitmarkCountStructure = structure[0]
                val hitbarCountStructure = structure[1]
                val hitbarPercentageStructure = structure[2]
                val hitbarToPercentageStructure = structure[3]

                val hits = other.blockBuffer.hits
                val hitbars = hits.filter { it.hitbar != null }

                buf.put(
                    hitmarkCountStructure.type,
                    hitmarkCountStructure.order,
                    hitmarkCountStructure.transformation,
                    hits.size
                )
                hits.forEach { hit ->
                    val hitmarks = Math.min(2, hit.hitmarks.size)

                    /*
                     * Inform the client of how many hitmarkers to decode.
                     */
                    if (hitmarks == 0) {
                        buf.putSmart(32766)
                    } else if (hitmarks > 1) {
                        buf.putSmart(32767)
                    }

                    for (i in 0 until hitmarks) {
                        val hitmark = hit.hitmarks[i]
                        buf.putSmart(hitmark.type)
                        buf.putSmart(hitmark.damage)
                    }

                    buf.putSmart(hit.clientDelay)
                }

                buf.put(
                    hitbarCountStructure.type,
                    hitbarCountStructure.order,
                    hitbarCountStructure.transformation,
                    hitbars.size
                )
                hitbars.forEach { hit ->
                    val hitbar = hit.hitbar!!
                    buf.putSmart(hitbar.type)
                    buf.putSmart(hitbar.depleteSpeed)

                    if (hitbar.depleteSpeed != 32767) {
                        var percentage = hitbar.percentage
                        if (percentage == -1) {
                            val max = other.getMaxHp()
                            val curr = Math.min(max, other.getCurrentHp())
                            percentage =
                                if (max == 0) 0 else ((curr.toDouble() * hitbar.maxPercentage.toDouble() / max.toDouble())).toInt()
                            if (percentage == 0 && curr > 0) {
                                percentage = 1
                            }
                        }

                        buf.putSmart(hitbar.delay)
                        buf.put(
                            hitbarPercentageStructure.type,
                            hitbarPercentageStructure.order,
                            hitbarPercentageStructure.transformation,
                            percentage
                        )
                        if (hitbar.depleteSpeed > 0) {
                            buf.put(
                                hitbarToPercentageStructure.type,
                                hitbarToPercentageStructure.order,
                                hitbarToPercentageStructure.transformation,
                                0
                            )
                        }
                    }
                }
            }

            UpdateBlockType.FACE_PAWN -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(
                    structure[0].type,
                    structure[0].order,
                    structure[0].transformation,
                    other.blockBuffer.facePawnIndex
                )
                buf.put(
                    structure[1].type,
                    structure[1].order,
                    structure[1].transformation,
                    other.blockBuffer.facePawnIndex shr 16
                )
            }

            UpdateBlockType.ANIMATION -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(structure[0].type, structure[0].order, structure[0].transformation, other.blockBuffer.animation)
                buf.put(
                    structure[1].type,
                    structure[1].order,
                    structure[1].transformation,
                    other.blockBuffer.animationDelay
                )
            }

            UpdateBlockType.GFX -> {
                val structure = blocks.updateBlocks[blockType]!!.values

                buf.put(DataType.BYTE, DataTransformation.SUBTRACT, 1) // gfx_array_how many to read one by one
                buf.put(DataType.BYTE, DataTransformation.ADD, 0) // gfx_index from the array
                buf.put(structure[0].type, structure[0].order, structure[0].transformation, other.blockBuffer.graphicId)
                buf.put(structure[1].type, structure[1].order, structure[1].transformation,
                    (other.blockBuffer.graphicHeight shl 16) or other.blockBuffer.graphicDelay
                )
            }

            UpdateBlockType.FORCE_MOVEMENT -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(
                    structure[0].type,
                    structure[0].order,
                    structure[0].transformation,
                    other.blockBuffer.forceMovement.diffX1
                )
                buf.put(
                    structure[1].type,
                    structure[1].order,
                    structure[1].transformation,
                    other.blockBuffer.forceMovement.diffZ1
                )
                buf.put(
                    structure[2].type,
                    structure[2].order,
                    structure[2].transformation,
                    other.blockBuffer.forceMovement.diffX2
                )
                buf.put(
                    structure[3].type,
                    structure[3].order,
                    structure[3].transformation,
                    other.blockBuffer.forceMovement.diffZ2
                )
                buf.put(
                    structure[4].type,
                    structure[4].order,
                    structure[4].transformation,
                    other.blockBuffer.forceMovement.clientDuration1
                )
                buf.put(
                    structure[5].type,
                    structure[5].order,
                    structure[5].transformation,
                    other.blockBuffer.forceMovement.clientDuration2
                )
                buf.put(
                    structure[6].type,
                    structure[6].order,
                    structure[6].transformation,
                    other.blockBuffer.forceMovement.directionAngle
                )
            }

            UpdateBlockType.APPLY_TINT -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(
                    structure[0].type,
                    structure[0].order,
                    structure[0].transformation,
                    other.blockBuffer.recolourStartCycle
                ) // recolourStartCycle
                buf.put(
                    structure[1].type,
                    structure[1].order,
                    structure[1].transformation,
                    other.blockBuffer.recolourEndCycle
                ) // recolourEndCycle
                buf.put(
                    structure[2].type,
                    structure[2].order,
                    structure[2].transformation,
                    other.blockBuffer.recolourHue
                ) // recolourHue
                buf.put(
                    structure[3].type,
                    structure[3].order,
                    structure[3].transformation,
                    other.blockBuffer.recolourSaturation
                ) // recolourSaturation
                buf.put(
                    structure[4].type,
                    structure[4].order,
                    structure[4].transformation,
                    other.blockBuffer.recolourLuminance
                ) // recolourLuminance
                buf.put(
                    structure[5].type,
                    structure[5].order,
                    structure[5].transformation,
                    other.blockBuffer.recolourOpacity
                ) // recolourAmount
            }
            UpdateBlockType.TEMP_MOVEMENT -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                //buf.put(structure[0].type, structure[0].order, structure[0].transformation, 1)
            }
            UpdateBlockType.NAME_CHANGE -> {
                //val structure = blocks.updateBlocks[blockType]!!.values
                //buf.put(structure[0].type, structure[0].order, structure[0].transformation, 0)
            }

            else -> throw RuntimeException("Unhandled update block type: $blockType")
        }
    }
}
