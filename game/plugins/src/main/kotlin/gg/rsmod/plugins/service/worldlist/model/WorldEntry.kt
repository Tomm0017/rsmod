package gg.rsmod.plugins.service.worldlist.model

import java.util.*

/**
 * @author Triston Plummer ("Dread")
 *
 * @param id        The id of the game world
 * @param types     A set containing the [WorldType]s for this entry
 * @param address   The ip address of the world
 * @param activity  The activity of the world
 * @param location  The location of the world
 * @param players   The number of players on this world
 */
data class WorldEntry(val id: Int, val types: EnumSet<WorldType>, val address: String, val activity: String, val location: WorldLocation, var players: Int)