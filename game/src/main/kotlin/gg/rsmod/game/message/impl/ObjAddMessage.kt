package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ObjAddMessage(val item: Int, val amount: Int, val tile: Int) : Message