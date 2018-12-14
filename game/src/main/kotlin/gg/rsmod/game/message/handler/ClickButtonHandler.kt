package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClickButtonMessage
import gg.rsmod.game.model.entity.Client
import org.apache.logging.log4j.LogManager

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickButtonHandler : MessageHandler<ClickButtonMessage> {

    companion object {
        private val logger = LogManager.getLogger(ClickButtonHandler::class.java)
    }

    override fun handle(client: Client, message: ClickButtonMessage) {
        val parent = message.hash shr 16
        val child = message.hash and 0xFFFF
        log(client, "Click button: parent={}, child={}, slot={}, item={}", parent, child, message.slot, message.item)

        if (!client.interfaces.isVisible(parent)) {
            logger.warn("Player '{}' tried to click button {} on a non-visible interface {}.", client.username, child, parent)
            return
        }

        if (client.world.plugins.executeButton(client, parent, child)) {
            return
        }

        if (client.world.devContext.debugButtons) {
            client.message("Unhandled button action: [parent=$parent, child=$child, slot=${message.slot}, item=${message.item}]")
        }
    }
}