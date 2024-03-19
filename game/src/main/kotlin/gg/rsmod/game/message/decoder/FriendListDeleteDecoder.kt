package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.FriendListDeleteMessage

class FriendListDeleteDecoder : MessageDecoder<FriendListDeleteMessage>() {
    override fun decode(
        opcode: Int,
        opcodeIndex: Int,
        values: HashMap<String, Number>,
        stringValues: HashMap<String, String>
    ): FriendListDeleteMessage {
        val name = stringValues["name"]!!.toString()
        return FriendListDeleteMessage(name)
    }
}