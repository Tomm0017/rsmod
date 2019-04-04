package gg.rsmod.game.service.login

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.service.GameService
import gg.rsmod.game.service.serializer.PlayerLoadResult
import gg.rsmod.net.codec.login.LoginResponse
import gg.rsmod.net.codec.login.LoginResultType
import gg.rsmod.util.io.IsaacRandom
import io.netty.channel.ChannelFutureListener
import mu.KLogging

/**
 * A worker for the [LoginService] that is responsible for handling the most
 * recent, non-handled [LoginServiceRequest] from its [boss].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LoginWorker(private val boss: LoginService) : Runnable {

    override fun run() {
        while (true) {
            val request = boss.requests.take()
            try {
                val world = request.world

                val client = Client.fromRequest(world, request.login)
                val loadResult: PlayerLoadResult = boss.serializer.loadClientData(client, request.login)

                if (loadResult == PlayerLoadResult.LOAD_ACCOUNT || loadResult == PlayerLoadResult.NEW_ACCOUNT) {
                    val decodeRandom = IsaacRandom(request.login.xteaKeys)
                    val encodeRandom = IsaacRandom(IntArray(request.login.xteaKeys.size) { request.login.xteaKeys[it] + 50 })

                    client.world.getService(GameService::class.java)?.submitGameThreadJob {
                        val loginResult: LoginResultType = when {
                            world.rebootTimer != -1 && world.rebootTimer < World.REJECT_LOGIN_REBOOT_THRESHOLD -> LoginResultType.SERVER_UPDATE
                            world.getPlayerForName(client.username) != null -> LoginResultType.ALREADY_ONLINE
                            world.players.count() >= world.players.capacity -> LoginResultType.MAX_PLAYERS
                            else -> if (client.register()) LoginResultType.ACCEPTABLE else LoginResultType.COULD_NOT_COMPLETE_LOGIN
                        }
                        if (loginResult == LoginResultType.ACCEPTABLE) {
                            client.channel.write(LoginResponse(index = client.index, privilege = client.privilege.id))
                            boss.successfulLogin(client, encodeRandom, decodeRandom)
                        } else {
                            request.login.channel.writeAndFlush(loginResult).addListener(ChannelFutureListener.CLOSE)
                            logger.info("User '{}' login denied with code {}.", client.username, loginResult)
                        }
                    }
                } else {
                    val errorCode = when (loadResult) {
                        PlayerLoadResult.INVALID_CREDENTIALS -> LoginResultType.INVALID_CREDENTIALS
                        PlayerLoadResult.INVALID_RECONNECTION -> LoginResultType.BAD_SESSION_ID
                        PlayerLoadResult.MALFORMED -> LoginResultType.ACCOUNT_LOCKED
                        else -> LoginResultType.COULD_NOT_COMPLETE_LOGIN
                    }
                    request.login.channel.writeAndFlush(errorCode).addListener(ChannelFutureListener.CLOSE)
                    logger.info("User '{}' login denied with code {} and channel {}.", client.username, loadResult, client.channel)
                }
            } catch (e: Exception) {
                logger.error("Error when handling request from ${request.login.channel}.", e)
            }
        }
    }

    companion object: KLogging()
}