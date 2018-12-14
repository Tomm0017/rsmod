package gg.rsmod.game.sync

import gg.rsmod.game.model.EntityType

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateBlockBuffer {

    private var mask = 0
    var forceChat = ""
    var faceDegrees = 0
    var animation = 0
    var animationDelay = 0
    var graphicId = 0
    var graphicHeight = 0
    var graphicDelay = 0

    fun isDirty(): Boolean = mask != 0

    fun clean() {
        mask = 0
    }

    fun addBlock(block: UpdateBlock, type: EntityType) {
        mask = mask or (if (type.isPlayer()) block.playerBit else block.npcBit)
    }

    fun hasBlock(block: UpdateBlock, type: EntityType): Boolean = (mask and (if (type.isPlayer()) block.playerBit else block.npcBit)) != 0

    fun blockValue(): Int = mask
}