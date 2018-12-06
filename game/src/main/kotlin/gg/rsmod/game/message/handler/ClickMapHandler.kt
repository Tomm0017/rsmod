package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClickMapMovementMessage
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMapHandler : MessageHandler<ClickMapMovementMessage> {

    override fun handle(client: Client, message: ClickMapMovementMessage) {
        client.tile = Tile(message.x, message.z)
        client.teleport = true
        client.interruptPlugins()

        log(client, "Click Map: x={}, z={}, type={}", message.x, message.z, message.movementType)
    }
}