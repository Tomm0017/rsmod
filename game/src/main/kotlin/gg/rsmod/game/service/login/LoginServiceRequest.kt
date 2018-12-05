package gg.rsmod.game.service.login

import gg.rsmod.game.model.World
import gg.rsmod.net.codec.login.LoginRequest

/**
 * Contains information required to process a [LoginRequest].
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class LoginServiceRequest(val world: World, val login: LoginRequest)