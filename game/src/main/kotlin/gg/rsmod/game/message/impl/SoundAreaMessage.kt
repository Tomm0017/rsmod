package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class SoundAreaMessage(val tileHash: Int, val id: Int, val radius: Int, val volume: Int, val delay: Int) : Message