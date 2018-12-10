package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ExamineObjectMessage
import gg.rsmod.game.model.entity.Client
import net.runelite.cache.definitions.ObjectDefinition

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ExamineObjectHandler : MessageHandler<ExamineObjectMessage> {

    override fun handle(client: Client, message: ExamineObjectMessage) {
        val id = message.id
        val def = client.world.definitions.get(ObjectDefinition::class.java, id)
        println("Examine object: $id, interactType=${def.interactType}")
    }
}