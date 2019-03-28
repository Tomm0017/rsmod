package gg.rsmod.game.sync.segment

import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.game.sync.block.UpdateBlockType
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcUpdateBlockSegment(private val npc: Npc, private val newAddition: Boolean) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        var mask = npc.blockBuffer.blockValue()
        val blocks = npc.world.npcUpdateBlocks

        var forceFacePawn = false
        var forceFaceTile = false

        if (newAddition) {

            if (npc.blockBuffer.faceDegrees != 0) {
                mask = mask or blocks.updateBlocks[UpdateBlockType.FACE_TILE]!!.bit
                forceFaceTile = true
            } else if (npc.blockBuffer.facePawnIndex != -1) {
                mask = mask or blocks.updateBlocks[UpdateBlockType.FACE_PAWN]!!.bit
                forceFacePawn = true
            }
        }

        buf.put(DataType.BYTE, mask and 0xFF)

        blocks.updateBlockOrder.forEach { blockType ->
            val force = when (blockType) {
                UpdateBlockType.FACE_TILE -> forceFaceTile
                UpdateBlockType.FACE_PAWN -> forceFacePawn
                else -> false
            }
            if (npc.hasBlock(blockType) || force) {
                write(buf, blockType)
            }
        }
    }

    private fun write(buf: GamePacketBuilder, blockType: UpdateBlockType) {
        val blocks = npc.world.npcUpdateBlocks

        when (blockType) {

            UpdateBlockType.FACE_PAWN -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(structure[0].type, structure[0].order, structure[0].transformation, npc.blockBuffer.facePawnIndex)
            }

            UpdateBlockType.FACE_TILE -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                val x = npc.blockBuffer.faceDegrees shr 16
                val z = npc.blockBuffer.faceDegrees and 0xFFFF
                buf.put(structure[0].type, structure[0].order, structure[0].transformation, x)
                buf.put(structure[1].type, structure[1].order, structure[1].transformation, z)
            }

            UpdateBlockType.ANIMATION -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(structure[0].type, structure[0].order, structure[0].transformation, npc.blockBuffer.animation)
                buf.put(structure[1].type, structure[1].order, structure[1].transformation, npc.blockBuffer.animationDelay)
            }

            UpdateBlockType.APPEARANCE -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(structure[0].type, structure[0].order, structure[0].transformation, npc.getTransmogId())
            }

            UpdateBlockType.GFX -> {
                val structure = blocks.updateBlocks[blockType]!!.values
                buf.put(structure[0].type, structure[0].order, structure[0].transformation, npc.blockBuffer.graphicId)
                buf.put(structure[1].type, structure[1].order, structure[1].transformation, (npc.blockBuffer.graphicHeight shl 16) or npc.blockBuffer.graphicDelay)
            }

            UpdateBlockType.FORCE_CHAT -> {
                buf.putString(npc.blockBuffer.forceChat)
            }

            UpdateBlockType.HITMARK -> {
                val structure = blocks.updateBlocks[blockType]!!.values

                val hitmarkCountStructure = structure[0]
                val hitbarCountStructure = structure[1]
                val hitbarPercentageStructure = structure[2]
                val hitbarToPercentageStructure = structure[3]

                val hits = npc.blockBuffer.hits
                val hitbars = hits.filter { it.hitbar != null }

                buf.put(hitmarkCountStructure.type, hitmarkCountStructure.order, hitmarkCountStructure.transformation, hits.size)
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

                buf.put(hitbarCountStructure.type, hitbarCountStructure.order, hitbarCountStructure.transformation, hitbars.size)
                hitbars.forEach { hit ->
                    val hitbar = hit.hitbar!!
                    buf.putSmart(hitbar.type)
                    buf.putSmart(hitbar.depleteSpeed)

                    if (hitbar.depleteSpeed != 32767) {
                        var percentage = hitbar.percentage
                        if (percentage == -1) {
                            val max = npc.getMaxHp()
                            val curr = Math.min(max, npc.getCurrentHp())
                            percentage = if (max == 0) 0 else ((curr.toDouble() * hitbar.maxPercentage.toDouble() / max.toDouble())).toInt()
                            if (percentage == 0 && curr > 0) {
                                percentage = 1
                            }
                        }

                        buf.putSmart(hitbar.delay)
                        buf.put(hitbarPercentageStructure.type, hitbarPercentageStructure.order, hitbarPercentageStructure.transformation, percentage)
                        if (hitbar.depleteSpeed > 0) {
                            buf.put(hitbarToPercentageStructure.type, hitbarToPercentageStructure.order, hitbarToPercentageStructure.transformation, 0)
                        }
                    }
                }
            }

            else -> throw RuntimeException("Unhandled update block type: $blockType")
        }
    }
}