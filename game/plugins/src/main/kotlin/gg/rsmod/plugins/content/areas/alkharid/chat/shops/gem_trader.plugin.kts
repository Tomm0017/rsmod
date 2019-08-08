package gg.rsmod.plugins.content.areas.alkharid.chat.shops

    on_npc_option(npc = Npcs.GEM_TRADER, option = "talk-to") {
        player.queue { dialog(this) }
    }

    on_npc_option(npc = Npcs.GEM_TRADER, option = "trade") {
        open_shop(player)
    }

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Good day to you traveller. Would you be interested in buying some gems?", animation = 567)
    when (it.options("Yes please.", "No, thank you.")) {
        1 -> open_shop(it.player)
        2 -> {
            it.chatPlayer("No thank you.", animation = 567)
            it.chatNpc("Eh. suit yourself.", animation = 567)
        }
    }
}

fun open_shop(p: Player) {
    p.openShop("Gem Trader.")
}