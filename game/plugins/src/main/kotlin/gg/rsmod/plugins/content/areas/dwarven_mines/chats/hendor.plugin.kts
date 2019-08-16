package gg.rsmod.plugins.content.areas.dwarven_mines.chats

spawn_npc(npc = Npcs.HENDOR, x = 3031, z = 9747, walkRadius = 3, direction = Direction.SOUTH)

    on_npc_option(Npcs.HENDOR, option = "talk-to") {
        player.queue { dialog(this) }
    }

    on_npc_option(Npcs.HENDOR, option = "trade") {
        open_shop(player)
    }

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Hello there! If you have any ore to trade I'm always<br>buying.")
    when (it.options("Let's trade.", "Why don't you ever restock your shop?", "Goodbye.")) {
        1 -> open_shop(it.player)
        2 -> {
            it.chatPlayer("Why don't you ever restock your shop?")
            it.chatNpc("The only ores I sell are the ones that are sold to me.")
            it.chatNpc("Anything else?")
            when (it.options("Let's trade.", "Goodbye."))  {
                1 -> open_shop(it.player)
                2 -> {
                    it.chatPlayer("See you later.")
                    it.chatNpc("Until next time.")
                }
            }
        }
        3 -> {
            it.chatPlayer("See you later.")
            it.chatNpc("Until next time.")
        }
    }
}

fun open_shop(p: Player) {
    p.openShop("Hendor's Awesome Ores")
}