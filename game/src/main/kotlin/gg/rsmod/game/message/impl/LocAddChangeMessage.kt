package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class LocAddChangeMessage(val id: Int, val settings: Int, val tile: Int) : Message