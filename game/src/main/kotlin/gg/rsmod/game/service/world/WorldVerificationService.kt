package gg.rsmod.game.service.world

import gg.rsmod.game.Server
import gg.rsmod.game.model.PlayerUID
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.net.codec.login.LoginResultType
import gg.rsmod.util.ServerProperties

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface WorldVerificationService : Service {

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    /**
     * Intercept the login result on a player log-in.
     *
     * @return null if the player can log in successfully without
     */
    fun interceptLoginResult(world: World, uid: PlayerUID, displayName: String, loginName: String): LoginResultType?
}