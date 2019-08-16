package gg.rsmod.plugins.content.npcs.npcInfo.cows

on_obj_option(8689, "milk") {
    player.queue {
        if (!player.inventory.contains(Items.BUCKET)) {
            milkcow(this)
        } else {
            player.lock()
            player.animate(2305)
            player.inventory.remove(Items.BUCKET, 1)
            player.inventory.add(Items.BUCKET_OF_MILK, 1)
            wait(7)
            player.unlock()
        }
    }
}

on_obj_option(8689, "steal-cowbell") {
    player.queue { messageBox("You need to have started the Cold War quest to attempt this.") }
}

on_item_on_obj(obj = Objs.DAIRY_COW, item = Items.BUCKET_OF_MILK) {
    player.message("The cow doesn't want that.")
}

on_item_option(1927, "empty") {
    player.inventory.remove(Items.BUCKET_OF_MILK, 1)
    player.inventory.add(Items.BUCKET, 1)
}

suspend fun milkcow(it: QueueTask) {
    it.chatNpc(npc = 4628, title = "Gillie Groats the Milkmaid", message = "Tee hee! You've never milked a cow before, have you?")
    it.chatPlayer("Erm... No. How could you tell?")
    it.chatNpc(npc = 4628, title = "Gillie Groats the Milkmaid", message = "Because you're spilling milk all over the floor. What a<br>waste! You need something to hold the milk.")
    it.chatPlayer("Ah yes, I really should have guessed that one, shouldn't I?")
    it.chatNpc(npc = 4628, title = "Gillie Groats the Milkmaid", message = "You're from the city. aren't you... Try it again with an<br>empty bucket.")
    it.chatPlayer("Right, I'll do that.")
}