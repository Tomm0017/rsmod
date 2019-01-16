package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.NpcAttackActionMessage
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
        client.attack(npc)
    }
}