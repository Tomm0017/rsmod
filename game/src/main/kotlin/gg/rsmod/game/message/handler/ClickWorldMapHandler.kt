package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClickWorldMapMessage
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.priv.Privilege

/**
 * @author HolyRSPS <dagreenrs@gmail.com>
 */
class ClickWorldMapHandler : MessageHandler<ClickWorldMapMessage> {

    override fun handle(client: Client, world: World, message: ClickWorldMapMessage) {
        if (world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            log(client, "Click world map: %s", Tile.from30BitHash(message.data).toString())
            client.moveTo(Tile.from30BitHash(message.data))
        }
    }
}