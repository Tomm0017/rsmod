package gg.rsmod.plugins.content.areas.alkharid.chat

spawn_npc(Npcs.KARIM, 3272, 3181, 0, 3, Direction.EAST)

    on_npc_option(npc = Npcs.KARIM, option = "talk-to") {
        player.queue { dialog(this) }
    }

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Would you like to buy a nice kebab? Only one gold.", animation = 567)
    when (it.options("I think I'll give it a miss.", "Yes please.")) {
        1 -> it.chatPlayer("I think I'll give it a miss.", animation = 567)
        2 -> {
            it.chatPlayer("Yes please.", animation = 567)
            if (it.player.inventory.getItemCount(Items.COINS_995) >= 1) {
                it.player.inventory.remove(Items.COINS_995, 1)
                it.player.inventory.add(Items.KEBAB, 1)
            } else
                it.chatPlayer("Oopz, I forgot to bring any money with me.", animation = 554)
                it.chatNpc("Come back when you have some.", animation = 554)
        }
    }
}