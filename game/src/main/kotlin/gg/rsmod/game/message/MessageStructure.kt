package gg.rsmod.game.message

import gg.rsmod.net.packet.PacketType

/**
 * The structure of a [Message].
 *
 * @param type The [PacketType].
 *
 * @param opcode The message's unique id.
 *
 * @param length The length of the payload, if any.
 *
 * @param values The [MessageValue]s. The order is important as it will be
 * encoded and decoded based on its order. This is why it's using the
 * [LinkedHashMap] implementation where insert-order is maintained.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class MessageStructure(val type: PacketType, val opcode: Int, val length: Int, val values: LinkedHashMap<String, MessageValue>)