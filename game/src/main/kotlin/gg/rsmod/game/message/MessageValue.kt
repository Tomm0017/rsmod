package gg.rsmod.game.message

import gg.rsmod.net.packet.DataOrder
import gg.rsmod.net.packet.DataSignature
import gg.rsmod.net.packet.DataTransformation
import gg.rsmod.net.packet.DataType

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class MessageValue(val id: String, val order: DataOrder, val transformation: DataTransformation, val type: DataType,
                        val signature: DataSignature)