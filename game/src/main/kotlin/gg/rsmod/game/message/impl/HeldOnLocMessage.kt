package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

data class HeldOnLocMessage(val obj: Int, val x: Int, val z: Int, val slot: Int, val item: Int, val movementType: Int) : Message