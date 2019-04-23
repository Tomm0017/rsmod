package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpNpc6Message
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc6Handler : MessageHandler<OpNpc6Message> {

    override fun handle(client: Client, world: World, message: OpNpc6Message) {
        world.sendExamine(client, message.npcId, ExamineEntityType.NPC)
    }
}