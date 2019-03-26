package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpNpc2Message
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.priv.Privilege

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc2Handler : MessageHandler<OpNpc2Message> {

    override fun handle(client: Client, message: OpNpc2Message) {
        val npc = client.world.npcs[message.index] ?: return

        if (!client.lock.canAttack()) {
            client.message(Entity.YOU_CANT_REACH_THAT)
            return
        }

        log(client, "Npc option 2: index=%d, movement=%d, npc=%s", message.index, message.movementType, npc)

        if (message.movementType == 1 && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(client.world.findRandomTileAround(npc.tile, 1) ?: npc.tile)
        }

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        client.attack(npc)
    }
}