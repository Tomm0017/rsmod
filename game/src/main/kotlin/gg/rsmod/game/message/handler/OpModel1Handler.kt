package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IfButtonMessage
import gg.rsmod.game.message.impl.OpModel1Message
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * For now, this will be treated as an IfButton1 with no option slot or item until I can find out more about this opcode.
 *
 * @author Curtis Woodard <nbness1337@gmail.com>
 */
class OpModel1Handler: MessageHandler<OpModel1Message> {
    override fun handle(client: Client, world: World, message: OpModel1Message) {
        val ifButtonMessage = IfButtonMessage(message.componentId, -1, -1, -1)
        IfButton1Handler().handle(client, world, ifButtonMessage)
    }
}