
import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.api.ext.setVarbit
import gg.rsmod.plugins.osrs.content.mechanics.multi.MultiwayCombat

MultiwayCombat.MULTI_REGIONS.forEach { region ->
    onRegionEnter(region) {
        it.player().setVarbit(MultiwayCombat.MULTIWAY_VARBIT, 1)
    }

    onRegionExit(region) {
        it.player().setVarbit(MultiwayCombat.MULTIWAY_VARBIT, 1)
    }
}