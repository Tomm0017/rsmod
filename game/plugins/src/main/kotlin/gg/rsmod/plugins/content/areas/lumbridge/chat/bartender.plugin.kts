package gg.rsmod.plugins.content.areas.lumbridge.chat

spawn_npc(Npcs.BARTENDER_7546, 3232, 3241, 0, 0, Direction.WEST)

on_npc_option(npc = Npcs.BARTENDER_7546, option = "talk-to", lineOfSightDistance = 4) {
    player.queue { dialog() }
}

suspend fun QueueTask.dialog() {
    chatNpc("Welcome to the Sheared Ram. What can I do for you?")
    when (options("I'll have a beer please.", "Heard any rumors recently?", "Nothing, I'm fine.")) {
        1 -> {
            chatPlayer("I'll have a beer please.")
            chatNpc("That'll be two coins please.")
            if (player.inventory.contains(Items.COINS_995)) {
                player.inventory.remove(Items.COINS_995, 2)
                player.inventory.add(Items.BEER, 1)
            } else
                chatPlayer("Oh dear, I don't seem to have enough money.")
        }
        2 -> {
            chatPlayer("Heard any rumors recently?")
            chatNpc("One of the patrons here is looking for treasure<br><br>apparently. A chap byu the name of Veos.")
        }
        3 -> chatPlayer("Nothing, I'm fine.")

    }
}