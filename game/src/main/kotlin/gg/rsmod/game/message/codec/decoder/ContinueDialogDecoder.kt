package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ContinueDialogMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ContinueDialogDecoder : MessageDecoder<ContinueDialogMessage>() {

    override fun decode(values: HashMap<String, Number>, stringValues: HashMap<String, String>): ContinueDialogMessage {
        val hash = values["hash"]!!.toInt()
        val slot = values["slot"]!!.toInt()
        return ContinueDialogMessage(parent = hash shr 16, child = hash and 0xFFFF, slot = slot)
    }
}