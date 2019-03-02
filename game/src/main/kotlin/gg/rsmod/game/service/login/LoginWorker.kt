package gg.rsmod.game.service.login

import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.service.GameService
import gg.rsmod.game.service.serializer.PlayerLoadResult
import gg.rsmod.net.codec.login.LoginResponse
import gg.rsmod.net.codec.login.LoginResultType
import gg.rsmod.util.io.IsaacRandom
import io.netty.channel.ChannelFutureListener
import mu.KotlinLogging

/**
 * A worker for the [LoginService] that is responsible for handling the most
 * recent, non-handled [LoginServiceRequest] from its [boss].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LoginWorker(private val boss: LoginService) : Runnable {

    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    override fun run() {
        while (true) {
            val request = boss.requests.take()
            try {
                if (request.login.crcs.filterIndexed { index, crc -> crc != request.login.crcs[index] }.isNotEmpty()) {
                    request.login.channel.writeAndFlush(LoginResultType.REVISION_MISMATCH).addListener(ChannelFutureListener.CLOSE)
                    continue
                }

                val client = Client.fromRequest(request.world, request.login)
                val loadResult: PlayerLoadResult = boss.serializer.loadClientData(client, request.login)

                if (loadResult == PlayerLoadResult.LOAD_ACCOUNT || loadResult == PlayerLoadResult.NEW_ACCOUNT) {
                    val world = request.world
                    val service = client.world.getService(GameService::class.java)!!
                    val encodeRandom = IsaacRandom(request.login.isaacSeed.map { it + 50 }.toIntArray())
                    val decodeRandom = IsaacRandom(request.login.isaacSeed)

                    service.submitGameThreadJob {
                        val loginResult: LoginResultType = when {
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
}