package gg.rsmod.game.sync.block

import gg.rsmod.game.model.ChatMessage
import gg.rsmod.game.model.ForcedMovement
import gg.rsmod.game.model.Hit

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateBlockBuffer {

    internal var teleport = false
    private var mask = 0

    var forceChat = ""
    lateinit var publicChat: ChatMessage

    var faceDegrees = 0
    var faceInstant = 0

    var facePawnIndex = -1

    var animation = 0


    /**
     * StartDelay
     */
    var animationDelay = 0

    var graphicId = 0
    var graphicHeight = 0
    var graphicDelay = 0

    var recolourStartCycle = 0
    var recolourEndCycle = 0
    var recolourHue = 0
    var recolourSaturation = 0
    var recolourLuminance = 0
    var recolourOpacity = 0

    var overrideLevel = 0

    var TempName = ""

    lateinit var forceMovement: ForcedMovement

    val hits = mutableListOf<Hit>()

    fun isDirty(): Boolean = mask != 0

    fun clean() {
        mask = 0
        teleport = false
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