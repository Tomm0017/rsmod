package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class OpHeldDMessage(val srcSlot: Int, val dstSlot: Int, val componentHash: Int, val insertMode: Boolean) : Message