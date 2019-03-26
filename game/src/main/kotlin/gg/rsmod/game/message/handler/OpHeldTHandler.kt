package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpHeldTMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeldTHandler : MessageHandler<OpHeldTMessage> {

    override fun handle(client: Client, message: OpHeldTMessage) {
        // TODO: handle magic on item
    }
}