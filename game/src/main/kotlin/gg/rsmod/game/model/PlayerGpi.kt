package gg.rsmod.game.model

import gg.rsmod.game.message.impl.LoginRegionMessage
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.service.xtea.XteaKeyService

/**
 * TODO: please delete this file, just using it temporarily until we have a
 * proper region/viewport system.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object PlayerGpi {

    var xteaKeys: XteaKeyService? = null

    fun init(client: Client) {
        if (xteaKeys == null) {
            xteaKeys = client.world.getService(XteaKeyService::class.java, false).get()
        }
        client.localPlayers.add(client)
        client.write(LoginRegionMessage(client.tile.x shr 3, client.tile.z shr 3, client.index, client.tile))
    }
}