package gg.rsmod.plugins.content.mechanics.multi

val MULTIWAY_VARBIT = 4605

on_world_init {
    world.getMultiCombatRegions().forEach { region ->
        on_enter_region(region) {
            player.setVarbit(MULTIWAY_VARBIT, 1)
        }

        on_exit_region(region) {
            player.setVarbit(MULTIWAY_VARBIT, 0)
        }
    }

    world.getMultiCombatChunks().forEach { chunk ->
        on_enter_chunk(chunk) {
            player.setVarbit(MULTIWAY_VARBIT, 1)
        }

        on_exit_chunk(chunk) {
            player.setVarbit(MULTIWAY_VARBIT, 0)
        }
    }
}