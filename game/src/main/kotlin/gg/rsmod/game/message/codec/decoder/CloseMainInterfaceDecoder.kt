package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.CloseMainInterfaceMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseMainInterfaceDecoder : MessageDecoder<CloseMainInterfaceMessage>() {

    override fun decode(values: HashMap<String, Number>, stringValues: HashMap<String, String>): CloseMainInterfaceMessage {
        return CloseMainInterfaceMessage()
    }
}