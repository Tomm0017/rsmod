package gg.rsmod.plugins.osrs.content

import gg.rsmod.game.model.Tile
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.player
import gg.rsmod.plugins.setVarbit

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MultiwayCombat {

    const val MULTIWAY_VARBIT = 4605

    private val MULTI_REGIONS = intArrayOf(
            9360, 9616, 9872, 9358, 9614, 9870, 9359, 9615, 9871 // Kruk's dungeon
    )

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        MULTI_REGIONS.forEach { region ->
            r.bindRegionEnter(region) {
                it.player().setVarbit(MULTIWAY_VARBIT, 1)
            }

            r.bindRegionExit(region) {
                it.player().setVarbit(MULTIWAY_VARBIT, 1)
            }
        }
    }

    fun isInMulti(t: Tile): Boolean = t.toRegionId() in MULTI_REGIONS
}