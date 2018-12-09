package gg.rsmod.game.service.serializer

import com.lambdaworks.crypto.SCryptUtil
import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.game.model.Tile
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

    private lateinit var startTile: Tile

    @Throws(Exception::class)
    final override fun init(server: Server, gameContext: GameContext, serviceProperties: ServerProperties) {
        startTile = Tile(gameContext.home)
        initSerializer(server, gameContext, serviceProperties)
    }

    override fun terminate(server: Server, gameContext: GameContext) {
    }

    fun onNewPlayer(client: Client, request: LoginRequest) {
        client.passwordHash = SCryptUtil.scrypt(request.password, 16384, 8, 1)
        client.tile = startTile
    }

    @Throws(Exception::class)
    abstract fun initSerializer(server: Server, gameContext: GameContext, serviceProperties: ServerProperties)

    abstract fun loadClientData(client: Client, request: LoginRequest): PlayerLoadResult

    abstract fun saveClientData(client: Client): Boolean
}