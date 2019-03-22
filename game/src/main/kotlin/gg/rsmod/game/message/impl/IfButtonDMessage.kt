package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class IfButtonDMessage(val srcComponentHash: Int, val srcSlot: Int, val srcItem: Int,
                            val dstComponentHash: Int, val dstSlot: Int, val dstItem: Int) : Message