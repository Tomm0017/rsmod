
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.setVarbit
import gg.rsmod.plugins.osrs.content.mechanics.multi.MultiwayCombat

MultiwayCombat.MULTI_REGIONS.forEach { region ->
    r.bindRegionEnter(region) {
        it.player().setVarbit(MultiwayCombat.MULTIWAY_VARBIT, 1)
    }

    r.bindRegionExit(region) {
        it.player().setVarbit(MultiwayCombat.MULTIWAY_VARBIT, 1)
    }
}