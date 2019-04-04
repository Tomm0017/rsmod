package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.WorldMapClickMessage
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Client

/**
 * @author HolyRSPS <dagreenrs@gmail.com>
 */
class ClickWorldMapHandler : MessageHandler<WorldMapClickMessage> {

    override fun handle(client: Client, message: WorldMapClickMessage) {
        val x = message.data shr 14 and 0x3FFF
        val y = message.data and 0x3FFF
        val z = message.data shr 28
        log(client, "Click world map: x=%d, y=%d, z=%d", x, y, z)
        client.moveTo(Tile(x, y, z))
    }
}