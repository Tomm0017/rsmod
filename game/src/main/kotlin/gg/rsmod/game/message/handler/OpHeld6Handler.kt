package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpHeld6Message
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeld6Handler : MessageHandler<OpHeld6Message> {

    override fun handle(client: Client, world: World, message: OpHeld6Message) {
        world.sendExamine(client, message.item, ExamineEntityType.ITEM)
    }
}