package gg.rsmod.game.system

import gg.rsmod.game.model.World
import gg.rsmod.game.service.login.LoginService
import gg.rsmod.net.codec.login.LoginRequest
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import org.apache.logging.log4j.LogManager

/**
 * A [ServerSystem] responsible for submitting the [LoginRequest] to be handled.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LoginSystem(override val channel: Channel, private val world: World)
    : ServerSystem(channel) {

    companion object {
        private val logger = LogManager.getLogger(LoginSystem::class.java)

        private var loginService: LoginService? = null
    }

    override fun receiveMessage(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is LoginRequest) {
            if (loginService == null) {
                loginService = world.getService(LoginService::class.java, false).get()
            }
            loginService!!.addLoginRequest(world, msg)
        }
    }

    override fun terminate() {
    }
}