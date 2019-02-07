package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ObjCountMessage(val item: Int, val oldAmount: Int, val newAmount: Int, val tile: Int) : Message