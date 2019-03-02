package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.MoveMinimapClickMessage
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.priv.Privilege

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMinimapHandler : MessageHandler<MoveMinimapClickMessage> {

    override fun handle(client: Client, message: MoveMinimapClickMessage) {
        if (!client.lock.canMove()) {
            return
        }

        log(client, "Click minimap: x=%d, z=%d, type=%d", message.x, message.z, message.movementType)

        client.closeInterfaceModal()
        client.interruptAllQueues()
        client.resetInteractions()

        if (message.movementType == 2 && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.teleport(message.x, message.z)
        } else {
            client.walkTo(message.x, message.z, if (message.movementType == 1)
                MovementQueue.StepType.FORCED_RUN else MovementQueue.StepType.NORMAL)
        }
    }
}