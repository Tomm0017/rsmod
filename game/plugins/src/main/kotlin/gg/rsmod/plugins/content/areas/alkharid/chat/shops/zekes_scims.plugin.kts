package gg.rsmod.plugins.content.areas.alkharid.chat.shops

    on_npc_option(npc = Npcs.ZEKE, option = "talk-to") {
        player.queue { dialog(this) }
    }

    on_npc_option(npc = Npcs.ZEKE, option = "trade") {
        open_shop(player)
    }

suspend fun dialog(it: QueueTask) {
    it.chatNpc("A thousand greetings, sir.", animation = 567)
    when (it.options("Do you want to trade?", "Nice cloak.", "Could you sell me a dragon scimitar?")) {
        1 -> {
            it.chatPlayer("Do you want to trade?", animation = 567)
            it.chatNpc("Yes, certainly. I deal in scimitars.", animation = 567)
            open_shop(it.player)
        }
        2 -> {
            it.chatPlayer("Nice cloak.", animation = 567)
            it.chatNpc("Thank you.", animation = 567)
        }
        3 -> {
            it.chatPlayer("Could you sell me a dragon scimitar?", animation = 567)
            it.chatNpc("a dragon scimitar? A DRAGON scimitar?", animation = 567)
            it.chatNpc("No way, man!", animation = 567)
            it.chatNpc("The banana-brained nitwits who make them would never dream of selling any to me.", animation = 567)
            it.chatNpc("Seriously, you'll be a monkey's uncle before you'll ever hold a dragon scimitar.", animation = 567)
            it.chatPlayer("Hmmm, funny you should say that...", animation = 567)
            it.chatNpc("Perhaps you'd like to take a look at my stock?", animation = 567)
            when (it.options("Yes please, Zeke.", "Not today, thank you.")) {
                1 -> open_shop(it.player)
                2 -> {
                    it.chatPlayer("Not today, thank you.", animation = 567)
                }
            }
        }
    }
}

fun open_shop(p: Player) {
    p.openShop("Zeke's Superior Scimitars.")
}