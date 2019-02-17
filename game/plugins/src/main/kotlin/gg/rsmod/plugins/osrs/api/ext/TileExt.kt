package gg.rsmod.plugins.osrs.api.ext

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun Tile.isMulti(world: World): Boolean {
    val region = toRegionId()
    val chunk = toChunkCoords().hashCode()
    return world.plugins.multiCombatChunks.contains(chunk) || world.plugins.multiCombatRegions.contains(region)
}