package gg.rsmod.plugins.osrs.content.npcs.sheep

import gg.rsmod.game.model.TimerKey
import gg.rsmod.plugins.osrs.api.ext.npc

val YELL_DELAY = TimerKey()

Sheep.SHEEP_NPCS.forEach { sheep ->
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