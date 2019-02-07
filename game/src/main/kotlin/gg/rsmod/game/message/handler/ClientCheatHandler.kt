package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClientCheatMessage
import gg.rsmod.game.model.entity.Client
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClientCheatHandler : MessageHandler<ClientCheatMessage> {

    override fun handle(client: Client, message: ClientCheatMessage) {
        val values = message.command.split(" ")
        val command = values[0].toLowerCase()
        val args = if (values.size > 1) values.slice(1 until values.size).filter { it.isNotEmpty() }.toTypedArray() else null
        log(client, "Command: cmd=%s, args=%s", command, Arrays.toString(args ?: emptyArray<String>()))

        if (!client.world.plugins.executeCommand(client, command, args)) {
            client.message("No valid command found: $command")
        }
    }
}