package gg.rsmod.game.service.serializer.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lambdaworks.crypto.SCryptUtil
import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.game.model.Privilege
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.service.serializer.PlayerLoadResult
import gg.rsmod.game.service.serializer.PlayerSaveData
import gg.rsmod.game.service.serializer.PlayerSerializerService
import gg.rsmod.net.codec.login.LoginRequest
import gg.rsmod.util.ServerProperties
import org.apache.logging.log4j.LogManager
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * A [PlayerSerializerService] implementation that decodes and encodes player
 * data in JSON.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class JsonPlayerSerializer : PlayerSerializerService() {

    companion object {
        private val logger = LogManager.getLogger(JsonPlayerSerializer::class.java)
    }

    private lateinit var path: Path

    private lateinit var startTile: Tile

    @Throws(Exception::class)
    override fun init(server: Server, gameContext: GameContext, serviceProperties: ServerProperties) {
        path = Paths.get(serviceProperties.get<String>("path")!!)
        startTile = Tile(gameContext.home)
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist: $path")
        }
    }

    override fun loadClientData(client: Client, request: LoginRequest): PlayerLoadResult {
        val save = path.resolve(client.loginUsername)
        if (!Files.exists(save)) {
            client.passwordHash = SCryptUtil.scrypt(request.password, 16384, 8, 1)
            client.tile = startTile
            saveClientData(client)
            return PlayerLoadResult.NEW_ACCOUNT
        }
        try {
            val reader = Files.newBufferedReader(save)
            val json = Gson()
            val data = json.fromJson<PlayerSaveData>(reader, PlayerSaveData::class.java)
            reader.close()

            if (!SCryptUtil.check(request.password, data.passwordHash)) {
                return PlayerLoadResult.INVALID_CREDENTIALS
            }

            client.loginUsername = data.username
            client.username = data.displayName
            client.passwordHash = data.passwordHash
            client.tile = data.tile
            client.privilege = client.world.gameContext.privileges.get(data.privilege) ?: Privilege.DEFAULT

            return PlayerLoadResult.LOAD_ACCOUNT
        } catch (e: Exception) {
            return PlayerLoadResult.MALFORMED
        }
    }

    override fun saveClientData(client: Client): Boolean {
        val data = PlayerSaveData(passwordHash = client.passwordHash, username = client.loginUsername,
                displayName = client.username, tile = client.tile, privilege = client.privilege.id)
        val writer = Files.newBufferedWriter(path.resolve(client.loginUsername))
        val json = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
        json.toJson(data, writer)
        writer.close()
        return true
    }

}