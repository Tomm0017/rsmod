package gg.rsmod.plugins.content.areas.alkharid.chat.shops

    on_npc_option(npc = Npcs.LOUIE_LEGS, option = "talk-to") {
        player.queue { dialog(this) }
    }

    on_npc_option(npc = Npcs.LOUIE_LEGS, option = "trade") {
        open_shop(player)
    }

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Hey, wanna buy some armour?", animation = 567)
    when (it.options("What have you got?", "No, thank you.")) {
        1 -> it.chatPlayer("No, thank you.", animation = 588)
        2 -> {
            it.chatPlayer("What have you got?", animation = 567)
            it.chatNpc("I provide items to help you keep your legs!", animation = 567)
            open_shop(it.player)
        }
    }
}

fun open_shop(p: Player) {
    p.openShop("Louie's Armoured Legs Bazaar.")
}