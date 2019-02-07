package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.OpHeld5Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeld5Decoder : MessageDecoder<OpHeld5Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpHeld5Message {
        val hash = values["hash"]!!.toInt()
        val slot = values["slot"]!!.toInt()
        val item = values["item"]!!.toInt()
        return OpHeld5Message(hash, slot, item)
    }
}