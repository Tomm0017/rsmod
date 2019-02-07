package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpLoc6Message
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpLoc6Handler : MessageHandler<OpLoc6Message> {

    override fun handle(client: Client, message: OpLoc6Message) {
        val id = message.id
        client.world.sendExamine(client, id, ExamineEntityType.OBJECT)
    }
}