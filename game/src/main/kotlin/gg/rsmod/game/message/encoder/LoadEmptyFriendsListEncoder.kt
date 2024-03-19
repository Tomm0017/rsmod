package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.FriendListLoadedMessage

class LoadEmptyFriendsListEncoder : MessageEncoder<FriendListLoadedMessage>() {

    override fun extract(message: FriendListLoadedMessage, key: String): Number = throw Exception("Unhandled value key.")

    override fun extractBytes(message: FriendListLoadedMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}