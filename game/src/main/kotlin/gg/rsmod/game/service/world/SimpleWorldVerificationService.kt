package gg.rsmod.game.service.world

import gg.rsmod.game.model.PlayerUID
import gg.rsmod.game.model.World
import gg.rsmod.net.codec.login.LoginResultType

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SimpleWorldVerificationService : WorldVerificationService {

    override fun interceptLoginResult(world: World, uid: PlayerUID, displayName: String, loginName: String): LoginResultType? = when {
        world.rebootTimer != -1 && world.rebootTimer < World.REJECT_LOGIN_REBOOT_THRESHOLD -> LoginResultType.SERVER_UPDATE
        world.getPlayerForName(displayName) != null -> LoginResultType.ALREADY_ONLINE
        world.players.count() >= world.players.capacity -> LoginResultType.MAX_PLAYERS
        else -> null
    }
}