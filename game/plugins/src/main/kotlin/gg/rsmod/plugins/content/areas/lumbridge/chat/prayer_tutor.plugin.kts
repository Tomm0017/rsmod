package gg.rsmod.plugins.content.areas.lumbridge.chat

spawn_npc(npc = Npcs.PRAYER_TUTOR, x = 3242, z = 3214, walkRadius = 3, height = 0)

on_npc_option(npc = Npcs.PRAYER_TUTOR, option = "talk-to") {
    player.queue { menu() }
}

suspend fun QueueTask.bones() {
    when (options("Basic Bones", "Big Bones", "Goodbye.")) {
        1 -> {
            itemMessageBox("Basic bones are left by many creatures such as goblins,<br>monkeys and that sort of thing. They won't get you<br>much when you bury them, but if you do it every time<br>you come across them, it all adds up!", item = Items.BONES, amountOrZoom = 400)
            bones()
        }
        2 -> {
            itemMessageBox("Big bones you can get by killing things like ogres and<br>giants, them being big things and all. They're quite a<br>good boost for your prayer if you are up to fighting<br>the big boys.", item = Items.BIG_BONES, amountOrZoom = 400)
            itemMessageBox("You can probably find them in caves and such dark<br>dank places.", item = Items.BIG_BONES, amountOrZoom = 400)
            bones()
        }
        3 -> chatPlayer("Goodbye.", animation = 588)
    }
}

suspend fun QueueTask.menu() {
    when (options("Can you teach me the basics of prayer please?", "Tell me about different bones.", "Goodbye.")) {
        1 -> {
            chatPlayer("Can you teach me the basics of prayer please?", animation = 554)
            chatNpc("Of course young one. You can gain prayer experience<br>by burying bones. Most of the wretched creatures<br>around the lands will leave behind bones after you kill<br>them, simply pick them up then click on them in your", animation = 570)
            chatNpc("inventory to bury them.", animation = 567)
            chatNpc("Prayers benefit you by making you stronger, quicker<br>or protecting an item when you die, wonderful things<br>like that. The first one you will already have straight<br>after leaving the tutorial. It's called Thick Skin.", animation = 570)
            player.focusTab(GameframeTab.PRAYER)
            chatNpc("Check the Prayer side-panel to see your prayers.", animation = 567)
            chatNpc("To use a prayer, simply click on the prayer to activate<br>it. It will light up and your prayer points will drain<br>away slowly. To turn it off, simply click it again.", animation = 569)
            chatNpc("When you run out of prayer points, the prayer will<br>stop, and you will need to recharge at an altar such as<br>the one in this chapel.", animation = 569)
            itemMessageBox("Look for this icon on your minimap to find an altar to<br>recharge your prayer.", item = 9714, amountOrZoom = 400)
            menu()
        }
        2 -> {
            chatPlayer("Tell me about different bones.", animation = 554)
            bones()
        }
        3 -> chatPlayer("Goodbye.", animation = 588)
    }
}
