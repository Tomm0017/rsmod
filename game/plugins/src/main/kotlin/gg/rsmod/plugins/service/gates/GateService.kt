package gg.rsmod.plugins.service.gates

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.plugins.api.ext.appendToString
import gg.rsmod.util.ServerProperties
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import mu.KLogging
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class GateService : Service {

    val gates = ObjectArrayList<GateSet>()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val file = Paths.get(serviceProperties.get("gates") ?: "./data/cfg/gates/gates.json")
        Files.newBufferedReader(file).use { reader ->
            val gates = Gson().fromJson<ObjectArrayList<GateSet>>(reader, object: TypeToken<ObjectArrayList<GateSet>>() {}.type)
            this.gates.addAll(gates)
        }

        logger.info { "Loaded ${gates.size.appendToString("gate")}." }
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    companion object : KLogging()
}