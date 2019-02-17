package gg.rsmod.plugins.osrs.content.npcs.sheep

import gg.rsmod.game.model.TimerKey
import gg.rsmod.plugins.osrs.api.cfg.Npcs
import gg.rsmod.plugins.osrs.api.ext.npc

val YELL_DELAY = TimerKey()

arrayOf(Npcs.SHEEP, Npcs.SHEEP_2697, Npcs.SHEEP_2698, Npcs.SHEEP_2699,
        Npcs.SHEEP_2786, Npcs.SHEEP_2787, Npcs.SHEEP_2788, Npcs.SHEEP_2789,
        Npcs.SHEEP_2790, Npcs.SHEEP_2791, Npcs.SHEEP_2792, Npcs.SHEEP_2793,
        Npcs.SHEEP_2794, Npcs.SHEEP_2795, Npcs.SHEEP_2796, Npcs.SHEEP_2797,
        Npcs.SHEEP_2798, Npcs.SHEEP_2799, Npcs.SHEEP_2800, Npcs.SHEEP_2801,
        Npcs.SHEEP_2802, Npcs.SHEEP_2803, Npcs.SHEEP_2804).forEach { sheep ->

    on_npc_spawn(npc = sheep) {
        val npc = it.npc()
        npc.timers[YELL_DELAY] = npc.world.random(15..25)
    }
}

on_timer(YELL_DELAY) {
    val npc = it.npc()
    npc.forceChat("Baa!")
    npc.timers[YELL_DELAY] = npc.world.random(15..25)
}