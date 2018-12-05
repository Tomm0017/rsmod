package gg.rsmod.game.model

import gg.rsmod.util.ServerProperties
import java.util.*

/**
 * Stores all the [Privilege]s that were specified on the game's
 * [ServerProperties].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PrivilegeSet : Iterable<Privilege> {

    /**
     * The privileges.
     */
    private lateinit var values: MutableList<Privilege>

    override fun iterator(): Iterator<Privilege> = values.iterator()

    @Throws(Exception::class)
    fun load(properties: ServerProperties) {
        val privilegeDefinitions = properties.get<ArrayList<*>>("privileges")!!
        val definitions = arrayListOf<Privilege>()
        privilegeDefinitions.forEach { struct ->
            val values = struct as LinkedHashMap<*, *>
            val id = values["id"] as Int
            val power = values["power"] as Int
            val icon = if (values.containsKey("icon")) values["icon"] as Int else 0
            val name = values["name"] as String
            definitions.add(Privilege(id = id, power = power, icon = icon, name = name.toLowerCase()))
        }
        values = arrayListOf()
        values.addAll(definitions)
    }

    fun size(): Int = values.size

    fun get(id: Int): Privilege? = values.firstOrNull { it.id == id }

    fun get(name: String): Privilege? = values.firstOrNull { it.name == name.toLowerCase() }
}