package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpHeld6Message
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeld6Handler : MessageHandler<OpHeld6Message> {

    override fun handle(client: Client, message: OpHeld6Message) {
        client.world.sendExamine(client, message.item, ExamineEntityType.ITEM)
    }
}