package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

data class UpdateIgnoreListMessage(val online: Number, val username: String, val previousUsername: String) : Message