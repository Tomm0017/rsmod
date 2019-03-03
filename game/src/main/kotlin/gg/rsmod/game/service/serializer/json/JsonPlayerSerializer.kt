package gg.rsmod.game.service.serializer.json

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lambdaworks.crypto.SCryptUtil
import gg.rsmod.game.Server
import gg.rsmod.game.model.PlayerUID
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.game.model.timer.TimerKey
import gg.rsmod.game.service.serializer.PlayerLoadResult
import gg.rsmod.game.service.serializer.PlayerSaveData
import gg.rsmod.game.service.serializer.PlayerSerializerService
import gg.rsmod.net.codec.login.LoginRequest
import gg.rsmod.util.ServerProperties
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * A [PlayerSerializerService] implementation that decodes and encodes player
 * data in JSON.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class JsonPlayerSerializer : PlayerSerializerService() {

    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    private lateinit var path: Path

    override fun initSerializer(server: Server, world: World, serviceProperties: ServerProperties) {
        path = Paths.get(serviceProperties.get<String>("path")!!)
        if (!Files.exists(path)) {
            Files.createDirectory(path)
            logger.info("Path does not exist: $path, creating directory...")
        }
    }

    override fun loadClientData(client: Client, request: LoginRequest): PlayerLoadResult {
        val save = path.resolve(client.loginUsername)
        if (!Files.exists(save)) {
            configureNewPlayer(client, request)
            saveClientData(client)
            return PlayerLoadResult.NEW_ACCOUNT
        }
        try {
            val reader = Files.newBufferedReader(save)
            val json = Gson()
            val data = json.fromJson<PlayerSaveData>(reader, PlayerSaveData::class.java)
            reader.close()

            if (!request.reconnecting) {
                /**
                 * If the request isn't a 'reconnection' request, we have to
                 * verify the password is correct.
                 */
                if (!SCryptUtil.check(request.password, data.passwordHash)) {
                    return PlayerLoadResult.INVALID_CREDENTIALS
                }
            } else {
                if (!Arrays.equals(data.previousXteas, request.xteaKeys)) {
                    return PlayerLoadResult.INVALID_RECONNECTION
                }
            }

            client.loginUsername = data.username
            client.uid = PlayerUID(data.username)
            client.username = data.displayName
            client.passwordHash = data.passwordHash
            client.tile = Tile(data.x, data.z, data.height)
            client.privilege = client.world.privileges.get(data.privilege) ?: Privilege.DEFAULT
            client.runEnergy = data.runEnergy
            client.interfaces.displayMode = DisplayMode.values.firstOrNull { it.id == data.displayMode } ?: DisplayMode.FIXED
            data.skills.forEach { skill ->
                client.getSkills().setXp(skill.skill, skill.xp)
                client.getSkills().setCurrentLevel(skill.skill, skill.lvl)
            }
            client.inventory.setItems(data.inventory)
            client.equipment.setItems(data.equipment)
            client.bank.setItems(data.bank)
            data.attributes.forEach { key, value ->
                val attribute = AttributeKey<Any>(key)
                client.attr[attribute] = if (value is Double) value.toInt() else value
            }
            data.timers.forEach { timer ->
                var time = timer.timeLeft
                if (timer.tickOffline) {
                    val elapsed = System.currentTimeMillis() - timer.currentMs
                    val ticks = (elapsed / client.world.gameContext.cycleTime).toInt()
                    time -= ticks
                }
                val key = TimerKey(timer.identifier, timer.tickOffline)
                client.timers[key] = Math.max(0, time)
            }
            data.varps.forEach { varp ->
                client.varps.setState(varp.id, varp.state)
            }

            return PlayerLoadResult.LOAD_ACCOUNT
        } catch (e: Exception) {
            logger.error("Error when loading player: ${request.username}", e)
            return PlayerLoadResult.MALFORMED
        }
    }

    override fun saveClientData(client: Client): Boolean {
        val data = PlayerSaveData(passwordHash = client.passwordHash, username = client.loginUsername, previousXteas = client.currentXteaKeys,
                displayName = client.username, x = client.tile.x, z = client.tile.z, height = client.tile.height,
                privilege = client.privilege.id, runEnergy = client.runEnergy, displayMode = client.interfaces.displayMode.id,
                skills = getSkills(client), inventory = client.inventory.toMap(), equipment = client.equipment.toMap(),
                bank = client.bank.toMap(), attributes = client.attr.toPersistentMap(),
                timers = client.timers.toPersistentTimers(), varps = client.varps.getAll().filter { it.state != 0 })
        val writer = Files.newBufferedWriter(path.resolve(client.loginUsername))
        val json = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
        json.toJson(data, writer)
        writer.close()
        return true
    }

    private fun getSkills(client: Client): List<PersistentSkill> {
        val skills = arrayListOf<PersistentSkill>()

        for (i in 0 until client.getSkills().maxSkills) {
            val xp = client.getSkills().getCurrentXp(i)
            val lvl = client.getSkills().getCurrentLevel(i)

            skills.add(PersistentSkill(skill = i, xp = xp, lvl = lvl))
        }

        return skills
    }

    data class PersistentSkill(@JsonProperty("skill") val skill: Int,
                               @JsonProperty("xp") val xp: Double,
                               @JsonProperty("lvl") val lvl: Int)
}