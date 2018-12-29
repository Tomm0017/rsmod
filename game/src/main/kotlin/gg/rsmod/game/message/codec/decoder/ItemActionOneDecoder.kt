package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ItemActionOneMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemActionOneDecoder : MessageDecoder<ItemActionOneMessage>() {

    override fun decode(values: HashMap<String, Number>, stringValues: HashMap<String, String>): ItemActionOneMessage {
        val hash = values["interfaceHash"]!!.toInt()
        val item = values["item"]!!.toInt()
        val slot = values["slot"]!!.toInt()

        return ItemActionOneMessage(if (item == 0xFFFF) -1 else item,
                if (slot == 0xFFFF) -1 else slot, hash)
    }
}