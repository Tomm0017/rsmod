package gg.rsmod.plugins.content.areas.edgeville.chat

on_npc_option(npc = Npcs.RICHARD_2200, option = "talk-to") {
    player.queue { chat(this) }
}

suspend fun chat(it: QueueTask) {
    it.chatNpc("Hello there, are you interested in buying one of my<br>special capes?", animation = 568)
    options(it)
}

suspend fun options(it: QueueTask) {
    when (it.options("What's so special about your capes?", "Yes please!", "No thanks.")) {
        1 -> {
            it.chatPlayer("What's so special about your capes?", animation = 554)
            it.chatNpc("Ahh well they make it less likely that you'll accidently<br>attack anyone wearing the same cape as you and easier<br>to attack everyone else. They also make it easier to<br>distinguish people who're wearing the same cape as you", animation = 570)
            it.chatNpc("from everyone else. They're very useful when out in<br>the wilderness with friends or anyone else you don't<br>want to harm.", animation = 569)
            it.chatNpc("So would you like to buy one?", animation = 567)
            when (it.options("Yes please!", "No thanks.")) {
                1 -> open_shop(it.player)
                2 -> no_thanks(it)
            }
        }
        2 -> open_shop(it.player)
        3 -> no_thanks(it)
    }
}

fun open_shop(p: Player) {
    p.openShop("Richard's Wilderness Cape Shop.")
}

suspend fun no_thanks(it: QueueTask) {
    it.chatPlayer("No thanks.", animation = 588)
}