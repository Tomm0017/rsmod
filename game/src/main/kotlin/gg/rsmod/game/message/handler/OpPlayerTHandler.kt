package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpPlayerTMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.INTERACTING_COMPONENT_CHILD
import gg.rsmod.game.model.attr.INTERACTING_COMPONENT_PARENT
import gg.rsmod.game.model.attr.INTERACTING_NPC_ATTR
import gg.rsmod.game.model.attr.INTERACTING_PLAYER_ATTR
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.priv.Privilege
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpPlayerTHandler : MessageHandler<OpPlayerTMessage> {

        override fun handle(client: Client, world: World, message: OpPlayerTMessage) {
                val index = message.index
                val parent = message.componentHash shr 16
                val child = message.componentHash and 0xFFFF

                if (!client.lock.canPlayerInteract()) {
                        return
                }
                val other = world.players[index] ?: return

                log(client, "Spell on player: player=%d. index=%d, component=[%d:%d], movement=%d", other.username, message.index, parent, child, message.movementType)

                client.interruptQueues()
                client.resetInteractions()

                if (message.movementType == 1 && world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
                        client.moveTo(world.findRandomTileAround(other.tile, 1) ?: other.tile)
                }

                client.closeInterfaceModal()
                client.interruptQueues()
                client.resetInteractions()

                client.attr[INTERACTING_PLAYER_ATTR] = WeakReference(other)
                client.attr[INTERACTING_COMPONENT_PARENT] = parent
                client.attr[INTERACTING_COMPONENT_CHILD] = child

                if (!world.plugins.executeSpellOnPlayer(client, parent, child)) {
                        client.writeMessage(Entity.NOTHING_INTERESTING_HAPPENS)
                        if (world.devContext.debugMagicSpells) {
                                client.writeMessage("Unhandled magic spell: [$parent, $child]")
                        }
                }
        }
}