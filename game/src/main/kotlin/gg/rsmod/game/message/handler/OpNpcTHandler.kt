package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpNpcTMessage
import gg.rsmod.game.model.attr.INTERACTING_COMPONENT_CHILD
import gg.rsmod.game.model.attr.INTERACTING_COMPONENT_PARENT
import gg.rsmod.game.model.attr.INTERACTING_NPC_ATTR
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.priv.Privilege
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpcTHandler : MessageHandler<OpNpcTMessage> {

    override fun handle(client: Client, message: OpNpcTMessage) {
        val npc = client.world.npcs[message.npcIndex] ?: return
        val parent = message.componentHash shr 16
        val child = message.componentHash and 0xFFFF

        if (!client.lock.canNpcInteract()) {
            client.message(Entity.YOU_CANT_REACH_THAT)
            return
        }

        log(client, "Spell on npc: npc=%d. index=%d, component=[%d, %d], movement=%d", npc.id, message.npcIndex, parent, child, message.movementType)

        client.interruptQueues()
        client.resetInteractions()

        if (message.movementType == 1 && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(client.world.findRandomTileAround(npc.tile, 1) ?: npc.tile)
        }

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        client.attr[INTERACTING_NPC_ATTR] = WeakReference(npc)
        client.attr[INTERACTING_COMPONENT_PARENT] = parent
        client.attr[INTERACTING_COMPONENT_CHILD] = child

        if (!client.world.plugins.executeSpellOnNpc(client, parent, child)) {
            client.message(Entity.NOTHING_INTERESTING_HAPPENS)
            if (client.world.devContext.debugMagicSpells) {
                client.message("Unhandled magic spell: [$parent, $child]")
            }
        }
    }
}