package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

data class UpdateFriendListMessage(val online: Number, val username: String, val previousUsername: String, val world: Int, val clanRank: Int, val rights: Int): Message