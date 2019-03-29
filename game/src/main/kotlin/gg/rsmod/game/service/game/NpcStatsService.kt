package gg.rsmod.game.service.game

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import mu.KLogging
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcStatsService : Service {

    private val definitions = hashMapOf<Int, NpcCombatDef>()

    private lateinit var path: Path

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        path = Paths.get(serviceProperties.getOrDefault("path", "./data/cfg/npc-stats.json"))
        if (!Files.exists(path)) {
            logger.warn("Definition file does not exist: $path")
            return
        }
        load()
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    fun get(npc: Int): NpcCombatDef? = definitions[npc]

    fun set(npc: Int, def: NpcCombatDef) {
        definitions[npc] = def
    }

    private fun load() {
        Files.newBufferedReader(path).use { reader ->
            val parsed = Gson().fromJson<Map<Int, NpcCombatDef>>(reader, object : TypeToken<Map<Int, NpcCombatDef>>() {}.type)
            if (parsed != null) {
                definitions.putAll(parsed)
            }
        }
        logger.info("Loaded {} npc combat definitions.", definitions.size)
    }

    companion object: KLogging()
}
