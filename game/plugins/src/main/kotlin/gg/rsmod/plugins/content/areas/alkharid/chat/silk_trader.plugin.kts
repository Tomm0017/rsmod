package gg.rsmod.plugins.content.areas.alkharid.chat

spawn_npc(Npcs.SILK_TRADER, 3299, 3204, 0, 2, Direction.EAST)

    on_npc_option(npc = Npcs.SILK_TRADER, option = "talk-to") {
        player.queue { dialog(this) }
    }

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Do you want to buy any fine silks?", animation = 567)
    when (it.options("How much are they?", "No. silk doesn't suit me.")) {
        1 -> {
            it.chatPlayer("How much are they?", animation = 567)
            it.chatNpc("3 gp.", animation = 567)
            when (it.options("No. That's to much for me.", "Okay, that sounds good.")) {
                1 -> {
                    it.chatPlayer("No. That's to much for me.", animation = 567)
                    it.chatNpc("2 gp and that's as low as I'll go.", animation = 567)
                    it.chatNpc("I'm not selling it for any less. You'll probably go and sell it in varrock for a profit, anyway.", animation = 567)
                    when (it.options("2 gp sounds good.", "No, really. I don't want it.")) {
                        1 -> {
                            it.chatPlayer("2 gp sounds good.", animation = 567)
                            if (it.player.inventory.getItemCount(Items.COINS_995) >= 2) {
                                it.player.inventory.remove(Items.COINS_995, 2)
                                it.player.inventory.add(Items.SILK, 1)
                                it.itemMessageBox("you buy some silk for 2 gp.", item = 950)
                            } else
                                it.chatPlayer("Oh dear, I don't have enough money.", animation = 554)
                                it.chatNpc("Well, come back when you do have some money!", animation = 567)
                        }
                        2 -> {
                            it.chatPlayer("No, really. I don't want it.", animation = 567)
                            it.chatNpc("Okay, but that's the best price you're going to get.", animation = 567)
                        }
                    }
                }
                2 -> {
                    it.chatPlayer("Okay, that sounds good.", animation = 567)
                    if (it.player.inventory.getItemCount(Items.COINS_995) >= 3) {
                        it.player.inventory.remove(Items.COINS_995, 3)
                        it.player.inventory.add(Items.SILK, 1)
                        it.itemMessageBox("you buy some silk for 3 gp.", item = 950)
                    } else
                        it.chatPlayer("Oh dear, I don't have enough money.", animation = 554)
                        it.chatNpc("Well, come back when you do have some money!", animation = 567)
                }
            }
        }
        2 -> it.chatPlayer("No. silk doesn't suit me.", animation = 567)
    }
}