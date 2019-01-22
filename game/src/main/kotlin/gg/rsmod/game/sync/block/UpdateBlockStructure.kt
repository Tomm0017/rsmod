package gg.rsmod.game.sync.block

import gg.rsmod.game.message.MessageValue

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class UpdateBlockStructure(val bit: Int, val values: List<MessageValue>)