package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpObj6Message
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

class OpObj6Handler : MessageHandler<OpObj6Message> {

    override fun handle(client: Client, world: World, message: OpObj6Message) {
        world.sendExamine(client, message.item, ExamineEntityType.ITEM)
    }
}