package gg.rsmod.game.sync

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateBlockBuffer {

    private var mask = 0

    var forceChat = ""

    var faceDegrees = 0

    fun isDirty(): Boolean = mask != 0

    fun clean() {
        mask = 0
    }

    fun addBlock(block: UpdateBlock) {
        mask = mask or block.value
    }

    fun hasBlock(block: UpdateBlock): Boolean = (mask and block.value) != 0

    fun blockValue(): Int = mask
}