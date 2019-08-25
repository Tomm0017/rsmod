package gg.rsmod.plugins.content.areas.varrock.chat

spawn_npc(npc = Npcs.IFFIE, x = 3204, z = 3419, direction = Direction.SOUTH)
spawn_npc(npc = Npcs.THESSALIA, x = 3205, z = 3417, walkRadius = 3, direction = Direction.EAST)

on_npc_option(npc = Npcs.THESSALIA, option = "talk-to") {
    player.queue { dialog(this) }
}
on_npc_option(npc = Npcs.THESSALIA, option = "trade") {
    open_shop(player)
}
on_npc_option(npc = Npcs.THESSALIA, option = "makeover") {
}

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Do you want to buy any fine clothes? Or would you like your scythe or bunny ears back?", animation = 567)
    when (it.options("What have you got?", "No, thank you.", "Can I have my scythe back?", "Can I have my bunny ears back?")) {
        1 -> {
            it.chatPlayer("What have you got?", animation = 588)
            it.chatNpc("Well, I have a number of fine pieces of clothing on sale or, if you prefer, I can offer you an exclusive, total-clothing makeover?", animation = 588)
            when (it.options("Tell me more about this makeover.", "I'd just like to buy some clothes")) {
                1 -> {
                    it.chatPlayer("Tell me more about this make-over.", animation = 588)
                    it.chatNpc("Certainly!", animation = 588)
                    it.chatNpc("Here at Thessalia's fine clothing boutique, we offer a unique service where we will totally revamp your outfit to you choosing.", animation = 588)
                    it.chatNpc("It costs only 500 coins. Tired of always wearing the same old outfit, day in, day out? This is the service for you!", animation = 588)
                    it.chatNpc("So what do you say? Interested? We can change either your top or your legwear!", animation = 588)
                }
                2 -> {
                    open_shop(it.player)
                }
            }
        }
        2 -> {
            it.chatPlayer("No, thank you.", animation = 588)
            it.chatNpc("Well, please return if you change your mind.", animation = 588)
        }
    }
}

fun open_shop(p: Player) {
    p.openShop("Thessalia's Fine Clothes.")
}