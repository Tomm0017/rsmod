package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ExamineObjectMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ExamineObjectHandler : MessageHandler<ExamineObjectMessage> {

    override fun handle(client: Client, message: ExamineObjectMessage) {
        val id = message.id
        println("Examine object: $id")
    }
}