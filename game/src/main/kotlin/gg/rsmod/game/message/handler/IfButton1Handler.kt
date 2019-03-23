package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IfButtonMessage
import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID
import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.attr.INTERACTING_SLOT_ATTR
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButton1Handler : MessageHandler<IfButtonMessage> {

    override fun handle(client: Client, message: IfButtonMessage) {
        val parent = message.hash shr 16
        val child = message.hash and 0xFFFF
        val option = message.option + 1

        if (!client.interfaces.isVisible(parent)) {
            return
        }

        log(client, "Click button: interfaceId=%d, component=%d, option=%d, slot=%d, item=%d", parent, child, option, message.slot, message.item)

        client.attr[INTERACTING_OPT_ATTR] = option
        client.attr[INTERACTING_ITEM_ID] = message.item
        client.attr[INTERACTING_SLOT_ATTR] = message.slot

        if (client.world.plugins.executeButton(client, parent, child)) {
            return
        }

        if (client.world.devContext.debugButtons) {
            client.message("Unhandled button action: [parent=$parent, child=$child, option=$option, slot=${message.slot}, item=${message.item}]")
        }
    }
}