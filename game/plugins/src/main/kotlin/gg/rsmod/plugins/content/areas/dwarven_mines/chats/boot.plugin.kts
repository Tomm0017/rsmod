package gg.rsmod.plugins.content.areas.dwarven_mines.chats

spawn_npc(Npcs.BOOT, 2985, 9812, 0, walkRadius = 5)

    on_npc_option(Npcs.BOOT, option = "talk-to") {
        player.queue { dialog(this) }
    }

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Hello tall person.")
    when (it.options("Hello short person.", "Why are you called boot?")) {
        1 -> it.chatPlayer("Hello short person.")
        2 -> {
            it.chatPlayer("Why are you called Boot?")
            it.chatNpc("I'm called Boot, because when I was very young, I<br>used to sleep, in a large boot.")
            it.chatPlayer("yeah, great, I didn't want your life story.")
        }
    }
}