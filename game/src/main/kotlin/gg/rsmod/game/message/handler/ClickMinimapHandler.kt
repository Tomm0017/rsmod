package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClickMinimapMessage
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.Privilege
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMinimapHandler : MessageHandler<ClickMinimapMessage> {

    override fun handle(client: Client, message: ClickMinimapMessage) {
        log(client, "Click Minimap: x={}, z={}, type={}", message.x, message.z, message.movementType)
        client.interruptPlugins()

        if (message.movementType == 2 && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.teleport(message.x, message.z)
        } else {
            client.walkTo(message.x, message.z, if (message.movementType == 1)
                MovementQueue.StepType.FORCED_RUN else MovementQueue.StepType.NORMAL)
        }
    }
}