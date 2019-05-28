package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class OpNpcUMessage(val componentHash: Int, val npcIndex: Int, val item: Int, val slot: Int, val movementType: Int) : Message