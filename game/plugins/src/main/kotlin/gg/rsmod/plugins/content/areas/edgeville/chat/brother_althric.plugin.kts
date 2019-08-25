package gg.rsmod.plugins.content.areas.edgeville.chat

spawn_npc(npc = Npcs.BROTHER_ALTHRIC, x = 3047, z = 3506, walkRadius = 3)

on_npc_option(npc = Npcs.BROTHER_ALTHRIC, option = "talk-to") {
    player.queue {
        dialog(this)
    }
}

suspend fun dialog(it: QueueTask) {
    it.chatPlayer("Very nice rosebushes you have here.", animation = 588)
    it.chatNpc("Yes, it has taken me many long hours in this garden to<br>bring them to this state of near-perfection.", animation = 589)
}