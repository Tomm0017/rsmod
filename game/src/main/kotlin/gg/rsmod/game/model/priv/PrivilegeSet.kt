package gg.rsmod.game.model.priv

import gg.rsmod.util.ServerProperties
import java.util.ArrayList
import java.util.LinkedHashMap

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

    fun load(properties: ServerProperties) {
        val privilegeDefinitions = properties.get<ArrayList<*>>("privileges")!!
        val definitions = mutableListOf<Privilege>()
        privilegeDefinitions.forEach { struct ->
            val values = struct as LinkedHashMap<*, *>
            val id = values["id"] as Int
            val icon = if (values.containsKey("icon")) values["icon"] as Int else 0
            val name = values["name"] as String
            val powerValues = values["powers"] as List<*>
            val powers = hashSetOf<String>()
            powerValues.forEach { power ->
                val value = power as String
                powers.add(value.toLowerCase())
            }
            definitions.add(Privilege(id = id, icon = icon, name = name.toLowerCase(), powers = powers))
        }
        values = mutableListOf()
        values.addAll(definitions)
    }

    fun size(): Int = values.size

    fun get(id: Int): Privilege? = values.firstOrNull { it.id == id }

    fun get(name: String): Privilege? = values.firstOrNull { it.name == name.toLowerCase() }

    fun isEligible(from: Privilege, to: String): Boolean = from.powers.contains(to.toLowerCase())
}