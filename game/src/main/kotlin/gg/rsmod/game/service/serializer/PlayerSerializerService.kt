package gg.rsmod.game.service.serializer

import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.service.Service
import gg.rsmod.net.codec.login.LoginRequest
import gg.rsmod.util.ServerProperties

/**
 * A [Service] that is responsible for encoding and decoding player data.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PlayerSerializerService : Service() {

    @Throws(Exception::class)
    override fun init(server: Server, gameContext: GameContext, serviceProperties: ServerProperties) {
    }

    override fun terminate(server: Server, gameContext: GameContext) {
    }

    abstract fun loadClientData(client: Client, request: LoginRequest): PlayerLoadResult

    abstract fun saveClientData(client: Client): Boolean
}