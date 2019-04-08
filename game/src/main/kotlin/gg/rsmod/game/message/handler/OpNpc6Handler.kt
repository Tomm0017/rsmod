package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpNpc6Message
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc6Handler : MessageHandler<OpNpc6Message> {

    override fun handle(client: Client, message: OpNpc6Message) {
        client.world.sendExamine(client, message.npcId, ExamineEntityType.NPC)
    }
}