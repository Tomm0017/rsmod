package gg.rsmod.game.service.world

import gg.rsmod.game.Server
import gg.rsmod.game.model.PlayerUID
import gg.rsmod.game.model.World
import gg.rsmod.net.codec.login.LoginResultType
import gg.rsmod.util.ServerProperties

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SimpleWorldVerificationService : WorldVerificationService {

    private lateinit var world: World

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        this.world = world
    }

    override fun interceptLoginResult(uid: PlayerUID, displayName: String, loginName: String): LoginResultType? = when {
        world.rebootTimer != -1 && world.rebootTimer < World.REJECT_LOGIN_REBOOT_THRESHOLD -> LoginResultType.SERVER_UPDATE
        world.getPlayerForName(displayName) != null -> LoginResultType.ALREADY_ONLINE
        world.players.count() >= world.players.capacity -> LoginResultType.MAX_PLAYERS
        else -> null
    }
}