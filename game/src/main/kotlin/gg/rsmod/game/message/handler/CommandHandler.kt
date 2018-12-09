package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.CommandMessage
import gg.rsmod.game.message.impl.SendChatboxTextMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CommandHandler : MessageHandler<CommandMessage> {

    override fun handle(client: Client, message: CommandMessage) {
        val values = message.command.split(" ")
        val command = values[0].toLowerCase()
        val args = if (values.size > 1) values.slice(1 until values.size).toTypedArray() else null
        log(client, "Command: cmd={}, args={}", command, args ?: emptyArray<String>())

        if (!client.world.plugins.executeCommand(client, command, args)) {
            client.write(SendChatboxTextMessage(type = 0, message = "No valid command found: $command", username = null))
        }
    }
}