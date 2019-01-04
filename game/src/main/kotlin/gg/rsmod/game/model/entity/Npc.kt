package gg.rsmod.game.model.entity

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.World
import gg.rsmod.game.sync.UpdateBlockType

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Npc(override val world: World) : Pawn(world) {

    override fun isDead(): Boolean = false

    override fun isRunning(): Boolean = false

    override fun addBlock(block: UpdateBlockType) {
        val bits = world.npcUpdateBlocks.updateBlocks[block]!!
        blockBuffer.addBit(bits.bit)
    }

    override fun hasBlock(block: UpdateBlockType): Boolean {
        val bits = world.npcUpdateBlocks.updateBlocks[block]!!
        return blockBuffer.hasBit(bits.bit)
    }

    override fun heal(amount: Int, capValue: Int) {

    }

    override fun cycle() {

    }

    override fun getType(): EntityType = EntityType.NPC
}