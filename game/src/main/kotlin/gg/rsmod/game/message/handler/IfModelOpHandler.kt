package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IfButtonMessage
import gg.rsmod.game.message.impl.IfModelOpMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @author bmyte <bmytescape@gmail.com>
 */
class IfModelOpHandler : MessageHandler<IfModelOpMessage> {

    override fun handle(client: Client, world: World, message: IfModelOpMessage) {
        IfButton1Handler().handle(client, world, IfButtonMessage(message.component, 0, -1, -1))
    }
}