package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IfButtonMessage
import gg.rsmod.game.message.impl.IfModelOp1Message
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

class IfModelOp1Handler : MessageHandler<IfModelOp1Message> {

    override fun handle(client: Client, world: World, message: IfModelOp1Message) {
        IfButton1Handler().handle(client, world, IfButtonMessage(message.component, 0, -1, -1))
    }
}