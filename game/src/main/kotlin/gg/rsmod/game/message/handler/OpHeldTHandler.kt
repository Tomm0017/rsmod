package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpHeldTMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.INTERACTING_ITEM
import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID
import gg.rsmod.game.model.attr.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.entity.Client
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeldTHandler : MessageHandler<OpHeldTMessage> {

    override fun handle(client: Client, world: World, message: OpHeldTMessage) {
        val fromComponentHash = message.fromComponentHash
        val fromInterfaceId = fromComponentHash shr 16
        val fromComponent = fromComponentHash and 0xFFFF

        val toComponentHash = message.toComponentHash
        val toInterfaceId = toComponentHash shr 16
        val toComponent = toComponentHash and 0xFFFF

        val itemId = message.item
        val itemSlot = message.itemSlot
        val unknown = message.spellSlot

        val item = client.inventory[itemSlot] ?: return

        if (item.id != itemId) {
            return
        }

        if (!client.lock.canInterfaceInteract()) {
            return
        }

        log(client, "Magic spell on item: from_component=[%d,%d], to_component=[%d,%d], unknown=%d, item=%d, item_slot=%d",
                fromInterfaceId, fromComponent, toInterfaceId, toComponent, unknown, itemId, itemSlot)

        client.attr[INTERACTING_ITEM] = WeakReference(item)
        client.attr[INTERACTING_ITEM_ID] = itemId
        client.attr[INTERACTING_ITEM_SLOT] = itemSlot

        val handled = world.plugins.executeSpellOnItem(client, fromComponentHash, toComponentHash)
        if (!handled && world.devContext.debugMagicSpells) {
            client.message("Unhandled spell on item: [item=[${item.id}, ${item.amount}], slot=$itemSlot, unknown=$unknown " +
                    "from_component=[$fromInterfaceId, $fromComponent], to_component=[$toInterfaceId, $toComponent]]")
        }
    }
}