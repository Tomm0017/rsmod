package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.DropItemMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DropItemDecoder : MessageDecoder<DropItemMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): DropItemMessage {
        val hash = values["hash"]!!.toInt()
        val slot = values["slot"]!!.toInt()
        val item = values["item"]!!.toInt()
        return DropItemMessage(hash, slot, item)
    }
}