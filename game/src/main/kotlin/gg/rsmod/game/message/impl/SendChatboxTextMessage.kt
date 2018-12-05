package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class SendChatboxTextMessage(val type: Int, val username: String?, val message: String) : Message