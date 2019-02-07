package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class OpNpcTMessage(val npcIndex: Int, val componentHash: Int, val componentSlot: Int, val movementType: Int) : Message