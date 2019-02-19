package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IfButtonMessage
import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID
import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.attr.INTERACTING_SLOT_ATTR
import gg.rsmod.game.model.entity.Client
import mu.KotlinLogging

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButton1Handler : MessageHandler<IfButtonMessage> {

    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    override fun handle(client: Client, message: IfButtonMessage) {
        val parent = message.hash shr 16
        val child = message.hash and 0xFFFF
        val option = message.option + 1

        log(client, "Click button: parent=%d, child=%d, option=%d, slot=%d, item=%d", parent, child, option, message.slot, message.item)

        if (!client.interfaces.isVisible(parent)) {
            logger.warn("Player '{}' tried to click button {} on a non-visible component {}.", client.username, child, parent)
            return
        }

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