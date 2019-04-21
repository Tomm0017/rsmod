package gg.rsmod.game.sync.block

import gg.rsmod.game.model.ChatMessage
import gg.rsmod.game.model.Hit

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateBlockBuffer {

    private var mask = 0

    var forceChat = ""
    lateinit var publicChat: ChatMessage

    var faceDegrees = 0
    var facePawnIndex = -1

    var animation = 0
    var animationDelay = 0

    var graphicId = 0
    var graphicHeight = 0
    var graphicDelay = 0

    val hits = mutableListOf<Hit>()

    fun isDirty(): Boolean = mask != 0

    fun clean() {
        mask = 0
        hits.clear()
    }

    fun addBit(bit: Int) {
        mask = mask or bit
    }

    fun hasBit(bit: Int): Boolean {
        return (mask and bit) != 0
    }

    fun blockValue(): Int = mask
}