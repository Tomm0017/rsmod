package gg.rsmod.game.sync.segment

import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.game.sync.UpdateBlockType
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcUpdateBlockSegment(private val npc: Npc, private val newAddition: Boolean) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        var mask = npc.blockBuffer.blockValue()
        val blocks = npc.world.npcUpdateBlocks

        var forceAppearance = false
        var forceFacePawn = false
        var forceFaceTile = false

        if (newAddition) {

            if (npc.getTransmogId() != -1) {
                mask = mask or blocks.updateBlocks[UpdateBlockType.APPEARANCE]!!.bit
                forceAppearance = true
            }

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
                UpdateBlockType.APPEARANCE -> forceAppearance
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
        }
    }
}