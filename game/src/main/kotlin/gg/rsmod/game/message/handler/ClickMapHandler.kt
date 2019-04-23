package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.MoveGameClickMessage
import gg.rsmod.game.message.impl.SetMapFlagMessage
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.game.model.timer.STUN_TIMER

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMapHandler : MessageHandler<MoveGameClickMessage> {

    override fun handle(client: Client, world: World, message: MoveGameClickMessage) {
        if (!client.lock.canMove()) {
            return
        }

        if (client.timers.has(STUN_TIMER)) {
            client.write(SetMapFlagMessage(255, 255))
            client.message(Entity.YOURE_STUNNED)
            return
        }

        log(client, "Click map: x=%d, z=%d, type=%d", message.x, message.z, message.movementType)

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        if (message.movementType == 2 && world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(message.x, message.z, client.tile.height)
        } else {
            client.walkTo(message.x, message.z, if (message.movementType == 1)
                MovementQueue.StepType.FORCED_RUN else MovementQueue.StepType.NORMAL)
        }
    }
}