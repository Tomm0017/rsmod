package gg.rsmod.game.message.handler

import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ExamineObjectMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ExamineObjectHandler : MessageHandler<ExamineObjectMessage> {

    override fun handle(client: Client, message: ExamineObjectMessage) {
        val id = message.id
        val def = client.world.definitions.get(ObjectDef::class.java, id)
        println("Examine object: $id, interactive=${def.interactive}, solid=${def.solid}")
    }
}