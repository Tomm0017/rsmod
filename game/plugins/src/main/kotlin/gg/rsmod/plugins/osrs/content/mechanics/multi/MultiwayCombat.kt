package gg.rsmod.plugins.osrs.content.mechanics.multi

import gg.rsmod.game.model.Tile

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MultiwayCombat {

    const val MULTIWAY_VARBIT = 4605

    val MULTI_REGIONS = intArrayOf(
            9360, 9616, 9872, 9358, 9614, 9870, 9359, 9615, 9871 // Kruk's dungeon
    )

    fun isInMulti(t: Tile): Boolean = t.toRegionId() in MULTI_REGIONS
}