package gg.rsmod.plugins.osrs.content.mechanics.multi

import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.api.ext.setVarbit

MultiwayCombat.MULTI_REGIONS.forEach { region ->
    on_enter_region(region) {
        it.player().setVarbit(MultiwayCombat.MULTIWAY_VARBIT, 1)
    }

    on_exit_region(region) {
        it.player().setVarbit(MultiwayCombat.MULTIWAY_VARBIT, 1)
    }
}