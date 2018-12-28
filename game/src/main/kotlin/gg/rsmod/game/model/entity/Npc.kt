package gg.rsmod.game.model.entity

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.World
import gg.rsmod.game.sync.UpdateBlock

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Npc(override val world: World) : Pawn(world) {

    override fun isDead(): Boolean = false

    override fun isRunning(): Boolean = false

    override fun addBlock(block: UpdateBlock) {
        val bits = world.updateBlocks[block]!!
        blockBuffer.addBit(bits.npcBit)
    }

    override fun hasBlock(block: UpdateBlock): Boolean {
        val bits = world.updateBlocks[block]!!
        return blockBuffer.hasBit(bits.npcBit)
    }

    override fun cycle() {

    }

    override fun getType(): EntityType = EntityType.NPC
}