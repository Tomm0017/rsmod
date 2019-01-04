package gg.rsmod.game.sync

import gg.rsmod.game.message.MessageValue

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class UpdateBlockStructure(val playerBit: Int, val npcBit: Int, val values: List<MessageValue>)