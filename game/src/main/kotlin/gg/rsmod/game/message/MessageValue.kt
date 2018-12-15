package gg.rsmod.game.message

import gg.rsmod.net.packet.DataOrder
import gg.rsmod.net.packet.DataSignature
import gg.rsmod.net.packet.DataTransformation
import gg.rsmod.net.packet.DataType

/**
 * A [MessageValue] represents a single value which can be operated on throughout
 * a [Message]. A [Message] can hold multiple [MessageValue]s.
 *
 * @param id
 * A unique name that will be used to decode and encode the value.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class MessageValue(val id: String, val order: DataOrder, val transformation: DataTransformation, val type: DataType,
                        val signature: DataSignature)