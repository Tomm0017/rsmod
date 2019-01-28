package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ItemActionTwoMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemActionTwoDecoder : MessageDecoder<ItemActionTwoMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ItemActionTwoMessage {
        val hash = values["component_hash"]!!.toInt()
        val item = values["item"]!!.toInt()
        val slot = values["slot"]!!.toInt()

        return ItemActionTwoMessage(if (item == 0xFFFF) -1 else item,
                if (slot == 0xFFFF) -1 else slot, hash)
    }
}