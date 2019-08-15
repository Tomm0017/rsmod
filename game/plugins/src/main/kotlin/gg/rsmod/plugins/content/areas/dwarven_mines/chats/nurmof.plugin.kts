package gg.rsmod.plugins.content.areas.dwarven_mines.chats

spawn_npc(Npcs.NURMOF, 2998, 9849, 0, walkRadius = 17)

    on_npc_option(Npcs.NURMOF, option = "talk-to") {
        player.queue { dialog(this) }
    }

    on_npc_option(Npcs.NURMOF, option = "trade") {
        open_shop(player)
    }

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Greetings and welcome to my pickaxe shop. Do you<br>want to buy my premium quality pickaxes?")
    when (it.options("Yes, please.", "No, thank you.", "Are your pickaxes better then other pickaxes, then?")) {
        1 -> open_shop(it.player)
        2 -> it.chatPlayer("No, thank you.")
        3 -> {
            it.chatPlayer("Are your pickaxes better then other pickaxes, then?")
            it.chatNpc("Of course they are! My pickaxes are made of highter<br>grade metal then your ordinary bronze pickaxes,<br>allowing you to mine that ore just a little bit faster than<br>normal.")
        }
    }
}

fun open_shop(p: Player) {
    p.openShop("Nurmof's Pickaxe Shop.")
}