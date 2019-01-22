package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class SpellOnNpcMessage(val npcIndex: Int, val interfaceHash: Int, val interfaceSlot: Int, val movementType: Int) : Message