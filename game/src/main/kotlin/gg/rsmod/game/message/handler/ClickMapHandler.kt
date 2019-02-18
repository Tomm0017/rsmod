package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.MoveGameClickMessage
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMapHandler : MessageHandler<MoveGameClickMessage> {

    override fun handle(client: Client, message: MoveGameClickMessage) {
        if (!client.lock.canMove()) {
            return
        }

        log(client, "Click map: x=%d, z=%d, type=%d", message.x, message.z, message.movementType)

        client.interruptPlugins()
        client.resetInteractions()

        if (message.movementType == 2 && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.teleport(message.x, message.z, client.tile.height)
        } else {
            client.walkTo(message.x, message.z, if (message.movementType == 1)
                MovementQueue.StepType.FORCED_RUN else MovementQueue.StepType.NORMAL)
        }
    }
}