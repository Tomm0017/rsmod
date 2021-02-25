package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

data class OpPlayerTMessage(val playerIndex: Int, val keydown: Boolean, val spellChildIndex: Int, val componentHash: Int) : Message