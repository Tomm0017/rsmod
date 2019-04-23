package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IfButtonMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID
import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.attr.INTERACTING_SLOT_ATTR
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButton1Handler : MessageHandler<IfButtonMessage> {

    override fun handle(client: Client, world: World, message: IfButtonMessage) {
        val interfaceId = message.hash shr 16
        val component = message.hash and 0xFFFF
        val option = message.option + 1

        if (!client.interfaces.isVisible(interfaceId)) {
            return
        }

        log(client, "Click button: component=[%d, %d], option=%d, slot=%d, item=%d", interfaceId, component, option, message.slot, message.item)

        client.attr[INTERACTING_OPT_ATTR] = option
        client.attr[INTERACTING_ITEM_ID] = message.item
        client.attr[INTERACTING_SLOT_ATTR] = message.slot

        if (world.plugins.executeButton(client, interfaceId, component)) {
            return
        }

        if (world.devContext.debugButtons) {
            client.message("Unhandled button action: [component=[$interfaceId, $component], option=$option, slot=${message.slot}, item=${message.item}]")
        }
    }
}