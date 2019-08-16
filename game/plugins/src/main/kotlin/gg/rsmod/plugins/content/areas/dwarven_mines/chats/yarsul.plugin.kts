package gg.rsmod.plugins.content.areas.dwarven_mines.chats

spawn_npc(npc = Npcs.YARSUL, x = 3031, z = 9747, walkRadius = 3, direction = Direction.SOUTH)

    on_npc_option(Npcs.YARSUL, option = "talk-to") {
        player.queue { dialog(this) }
    }

    on_npc_option(Npcs.YARSUL, option = "trade") {
        open_shop(player)
    }

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Good day to you, welcome to my pickaxe shop. Are you<br>intrested in making a purchase?")
    when (it.options("Yes, please.", "No thanks.")) {
        1 -> open_shop(it.player)
        2 -> {
            it.chatPlayer("No thanks.")
            it.chatNpc("Suit yourself. I'll be here if you change your mind.")
        }
    }
}

fun open_shop(p: Player) {
    p.openShop("Yarsul's Prodigious Pickaxes")
}