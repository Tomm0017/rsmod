package gg.rsmod.plugins.content.areas.alkharid.chat.shops

    on_npc_option(npc = Npcs.RANAEL, option = "talk-to") {
        player.queue { dialog(this) }

    on_npc_option(npc = Npcs.RANAEL, option = "trade") {
        open_shop(player)
    }
}

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Do you want to buy armoured skirts? Designed especially for ladies who like to fight.", animation = 567)
    when (it.options("Yes please.", "No thank you, that's not my scene.")) {
        1 -> open_shop(it.player)
        2 -> it.chatPlayer("No thank you, that's not my scene.", animation = 588)
    }
}

fun open_shop(p: Player) {
    p.openShop("Ranael's Super Skirt Store.")
}