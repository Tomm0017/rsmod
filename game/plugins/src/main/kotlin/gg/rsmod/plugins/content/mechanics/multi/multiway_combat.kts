package gg.rsmod.plugins.content.mechanics.multi

import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.api.ext.setVarbit

val MULTIWAY_VARBIT = 4605

on_world_init {
    world.multiCombatRegions.forEach { region ->
        on_enter_region(region) {
            it.player().setVarbit(MULTIWAY_VARBIT, 1)
        }

        on_exit_region(region) {
            it.player().setVarbit(MULTIWAY_VARBIT, 0)
        }
    }

    world.multiCombatChunks.forEach { chunk ->
        on_enter_chunk(chunk) {
            it.player().setVarbit(MULTIWAY_VARBIT, 1)
        }

        on_exit_chunk(chunk) {
            it.player().setVarbit(MULTIWAY_VARBIT, 0)
        }
    }
}