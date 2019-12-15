package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class OpPlayerTMessage(val index: Int, val componentHash: Int, val componentSlot: Int, val movementType: Int) : Message