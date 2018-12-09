package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.message.impl.CommandMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CommandHandler : MessageHandler<CommandMessage> {

    override fun handle(client: Client, message: CommandMessage) {
        val values = message.command.split(" ")
        val command = values[0].toLowerCase()
        val args = if (values.size > 1) values.slice(1 until values.size).toTypedArray() else null
        client.world.plugins.executeCommand(client, command, args)
        if (args != null) {
            log(client, "Command: cmd={}, args={}", command, args)
        } else {
            log(client, "Command: cmd={}, args={}", command)
        }
    }
}