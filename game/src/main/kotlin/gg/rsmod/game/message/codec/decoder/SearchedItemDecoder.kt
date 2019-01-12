package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.SearchedItemMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SearchedItemDecoder : MessageDecoder<SearchedItemMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): SearchedItemMessage {
        val item = values["item"]!!.toInt()
        return SearchedItemMessage(item)
    }
}