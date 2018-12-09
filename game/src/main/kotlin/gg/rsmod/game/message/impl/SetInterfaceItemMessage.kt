package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class SetInterfaceItemMessage(val hash: Int, val item: Int, val amount: Int) : Message