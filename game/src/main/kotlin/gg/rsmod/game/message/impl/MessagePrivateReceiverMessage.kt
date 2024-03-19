package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

data class MessagePrivateReceiverMessage(val target: String, val unknown: Int, val unknown2: Int, val rights: Int, val message: String) : Message