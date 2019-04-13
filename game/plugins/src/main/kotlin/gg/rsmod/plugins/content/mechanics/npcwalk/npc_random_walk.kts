package gg.rsmod.plugins.content.mechanics.npcwalk

import gg.rsmod.game.model.attr.FACING_PAWN_ATTR

val SEARCH_FOR_PATH_TIMER = TimerKey()
val SEARCH_FOR_PATH_DELAY = 15..30

on_global_npc_spawn {
    if (npc.walkRadius > 0) {
        npc.timers[SEARCH_FOR_PATH_TIMER] = world.random(SEARCH_FOR_PATH_DELAY)
    }
}

on_timer(SEARCH_FOR_PATH_TIMER) {
    if (npc.isActive()) {
        val facing = npc.attr[FACING_PAWN_ATTR]?.get()

        /*
         * The npc is not facing a player, so it can walk.
         */
        if (facing == null) {
            val rx = world.random(-npc.walkRadius..npc.walkRadius)
            val rz = world.random(-npc.walkRadius..npc.walkRadius)

            val start = npc.spawnTile
            val dest = start.transform(rx, rz)

            /*
             * Only walk to destination if the chunk has previously been created.
             */
            if (world.collision.chunks.get(dest, createIfNeeded = false) != null) {
                npc.walkTo(dest)
            }
        }
    }

    npc.timers[SEARCH_FOR_PATH_TIMER] = world.random(SEARCH_FOR_PATH_DELAY)
}