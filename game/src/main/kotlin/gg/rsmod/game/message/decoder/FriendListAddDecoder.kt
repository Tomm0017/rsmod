package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.FriendListAddMessage

class FriendListAddDecoder : MessageDecoder<FriendListAddMessage>() {
    override fun decode(
        opcode: Int,
        opcodeIndex: Int,
        values: HashMap<String, Number>,
        stringValues: HashMap<String, String>
    ): FriendListAddMessage {
        val name = stringValues["name"]!!.toString()
        return FriendListAddMessage(name)
    }
}