package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpLoc6Message
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpLoc6Handler : MessageHandler<OpLoc6Message> {

    override fun handle(client: Client, world: World, message: OpLoc6Message) {
        world.sendExamine(client, message.id, ExamineEntityType.OBJECT)
    }
}