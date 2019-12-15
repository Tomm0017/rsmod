package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

data class LoadFriendListMessage(val online: Int, val username: String, val previousUsername: String, val world : Int) : Message