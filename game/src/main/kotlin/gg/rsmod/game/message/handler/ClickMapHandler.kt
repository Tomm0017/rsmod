package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClickMapMessage
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMapHandler : MessageHandler<ClickMapMessage> {

    override fun handle(client: Client, message: ClickMapMessage) {
        log(client, "Click Map: x={}, z={}, type={}", message.x, message.z, message.movementType)
        client.interruptPlugins()

        if (message.movementType == 2 && client.world.gameContext.devMode) {
            client.teleport(message.x, message.z, client.tile.height)
        } else {
            client.walkTo(message.x, message.z, if (message.movementType == 1)
                MovementQueue.StepType.FORCED_RUN else MovementQueue.StepType.NORMAL)
        }
    }
}