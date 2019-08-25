package gg.rsmod.plugins.content.areas.varrock.chat

spawn_npc(npc = Npcs.GYPSY, x = 3203, z = 3424, walkRadius = 2, direction = Direction.EAST)

on_npc_option(npc = Npcs.GYPSY, option = "talk-to") {
    player.queue { dialog(this) }
}

suspend fun dialog(it: QueueTask) {
    when (it.options("The Demon Slayer Quest", "The Recipe For Disaster Quest")) {
        1 -> {
            it.chatNpc("Greetings young one.", animation = 588)
            it.chatNpc("You're a hero now. That was a good bit of demonslaying.", animation = 588)
            when (it.options("How do you know I killed it?", "Thanks.", "stop calling me that!")) {
                1 -> {
                    it.chatPlayer("How do you know i killed it?", animation = 588)
                    it.chatNpc("you forget. I'm good at knowing things.", animation = 588)
                }
                2 -> {
                    it.chatPlayer("Thanks.", animation = 588)
                }
                3 -> {
                    it.chatPlayer("Stop calling me that!", animation = 588)
                    it.chatNpc("In the scheme of things you are very young.", animation = 588)
                    when (it.options("Ok but how old are you?", "Oh if it's in the scheme of things that's ok.")) {
                        1 -> {
                            it.chatPlayer("Ok, but how old are you?", animation = 588)
                            it.chatNpc("Count the number of legs on the stools in the Blue Moon inn, and multiply that number by seven.", animation = 588)
                            it.chatPlayer("Er, yeah, whatever.", animation = 588)
                        }
                        2 -> {
                            it.chatPlayer("Oh if it's in the scheme of things that's ok.", animation = 588)
                            it.chatNpc("You show wisdom for one so young.", animation = 588)
                        }
                    }
                }
            }
        }
        2 -> {
            it.chatPlayer("There's just one thing I don't understand about that whole time travel culinaromancer thing...", animation = 588)
            it.chatNpc("Oh yes? What was that?", animation = 588)
            it.chatPlayer("Everything!", animation = 588)
            it.chatNpc("I wouldn't worry about it to much if I were you. The world is safe once more thanks to your assistance.", animation = 588)
            it.chatNpc("That's all that really matters.", animation = 588)
            it.chatPlayer("Yeah, I guess... I just wish things made sense is all...", animation = 588)
        }
    }
}