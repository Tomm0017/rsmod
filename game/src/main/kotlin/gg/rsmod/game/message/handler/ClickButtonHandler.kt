package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClickButtonMessage
import gg.rsmod.game.message.impl.SendChatboxTextMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickButtonHandler : MessageHandler<ClickButtonMessage> {

    override fun handle(client: Client, message: ClickButtonMessage) {
        val parent = message.hash shr 16
        val child = message.hash and 0xFFFF
        log(client, "Click button: parent={}, child={}, slot={}, item={}", parent, child, message.slot, message.item)

        if (client.world.server.getPlugins().executeButton(client, parent, child)) {
            return
        }

        if (client.world.gameContext.devMode) {
            val debug = "Unhandled button action: [parent=$parent, child=$child, slot=${message.slot}, item=${message.item}]"
            client.write(SendChatboxTextMessage(type = 0, message = debug, username = null))
        }
    }
}