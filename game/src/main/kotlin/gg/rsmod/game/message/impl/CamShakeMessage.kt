package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

data class CamShakeMessage(val index: Int, val left: Int, val center: Int, val right: Int) : Message
