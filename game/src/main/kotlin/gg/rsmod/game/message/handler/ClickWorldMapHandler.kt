package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClickWorldMapMessage
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.priv.Privilege

/**
 * @author HolyRSPS <dagreenrs@gmail.com>
 */
class ClickWorldMapHandler : MessageHandler<ClickWorldMapMessage> {

    override fun handle(client: Client, message: ClickWorldMapMessage) {
        if (client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            log(client, "Click world map: %s", Tile.from30BitHash(message.data).toString())
            client.moveTo(Tile.from30BitHash(message.data))
        }
    }
}