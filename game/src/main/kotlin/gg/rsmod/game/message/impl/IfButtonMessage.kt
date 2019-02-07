package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class IfButtonMessage(val hash: Int, val option: Int, val slot: Int, val item: Int) : Message