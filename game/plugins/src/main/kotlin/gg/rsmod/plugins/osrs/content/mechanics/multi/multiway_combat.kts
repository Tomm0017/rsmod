
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.setVarbit
import gg.rsmod.plugins.osrs.content.mechanics.multi.MultiwayCombat

MultiwayCombat.MULTI_REGIONS.forEach { region ->
    onRegionEnter(region) {
        it.player().setVarbit(MultiwayCombat.MULTIWAY_VARBIT, 1)
    }

    onRegionExit(region) {
        it.player().setVarbit(MultiwayCombat.MULTIWAY_VARBIT, 1)
    }
}