package gg.rsmod.plugins.osrs.content.mechanics.multi

import gg.rsmod.game.model.Tile

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MultiwayCombat {

    const val MULTIWAY_VARBIT = 4605

    // TODO: have multiway combat regions be defined by plugins instead of this array
    val MULTI_REGIONS = intArrayOf(
            9033,
            9360, 9616, 9872, 9358, 9614, 9870, 9359, 9615, 9871 // Kruk's dungeon
    )

    fun isInMulti(t: Tile): Boolean = t.toRegionId() in MULTI_REGIONS
}