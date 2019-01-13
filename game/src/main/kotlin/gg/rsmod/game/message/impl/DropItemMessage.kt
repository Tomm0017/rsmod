package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class DropItemMessage(val hash: Int, val slot: Int, val item: Int) : Message