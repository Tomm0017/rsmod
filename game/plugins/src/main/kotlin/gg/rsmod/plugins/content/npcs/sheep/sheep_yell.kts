package gg.rsmod.plugins.content.npcs.sheep

val YELL_DELAY = TimerKey()

Sheep.SHEEP_NPCS.forEach { sheep ->
    on_npc_spawn(npc = sheep) {
        val npc = npc
        npc.timers[YELL_DELAY] = world.random(15..25)
    }
}

on_timer(YELL_DELAY) {
    val npc = npc
    npc.forceChat("Baa!")
    npc.timers[YELL_DELAY] = world.random(15..25)
}