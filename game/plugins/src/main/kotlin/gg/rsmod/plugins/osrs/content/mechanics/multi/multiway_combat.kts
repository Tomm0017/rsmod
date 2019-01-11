import gg.rsmod.plugins.osrs.content.mechanics.multi.MultiwayCombat
import gg.rsmod.plugins.osrs.api.player
import gg.rsmod.plugins.osrs.api.setVarbit

/**
 * @author Tom <rspsmods@gmail.com>
 */

MultiwayCombat.MULTI_REGIONS.forEach { region ->
    r.bindRegionEnter(region) {
        it.player().setVarbit(MultiwayCombat.MULTIWAY_VARBIT, 1)
    }

    r.bindRegionExit(region) {
        it.player().setVarbit(MultiwayCombat.MULTIWAY_VARBIT, 1)
    }
}