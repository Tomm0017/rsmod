package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.NpcAttackActionMessage
import gg.rsmod.game.model.Privilege
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcAttackActionHandler : MessageHandler<NpcAttackActionMessage> {

    override fun handle(client: Client, message: NpcAttackActionMessage) {
        val npc = client.world.npcs.get(message.index) ?: return

        if (!client.lock.canAttack()) {
            return
        }

        log(client, "Npc attack: index=%d, movement=%d", message.index, message.movementType)

        if (message.movementType == 1 && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.teleport(client.world.findRandomTileAround(npc.tile, 1) ?: npc.tile)
        }

        client.attack(npc)
    }
}