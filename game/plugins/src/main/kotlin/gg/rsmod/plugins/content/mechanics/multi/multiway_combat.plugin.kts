package gg.rsmod.plugins.content.mechanics.multi


/**
 * To add multi combat region/chunk use :
 * set_multi_combat_region(regionId)
 * set_multi_combat_chunk(chunkHashCode)
 */
on_world_init {
    world.getMultiCombatRegions().forEach { region ->
        on_enter_region(region) {
            player.setVarbit(MultiData.MULTIWAY_VARBIT, 1)
        }

        on_exit_region(region) {
            player.setVarbit(MultiData.MULTIWAY_VARBIT, 0)
        }
    }

    world.getMultiCombatChunks().forEach { chunk ->
        on_enter_chunk(chunk) {
            player.setVarbit(MultiData.MULTIWAY_VARBIT, 1)
        }

        on_exit_chunk(chunk) {
            player.setVarbit(MultiData.MULTIWAY_VARBIT, 0)
        }
    }
}
