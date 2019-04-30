package gg.rsmod.plugins.content.npcs.man

val MEN = arrayOf(Npcs.MAN_3014, Npcs.MAN_3078, Npcs.MAN_3079, Npcs.MAN_3080,
        Npcs.MAN_3081, Npcs.MAN_3082, Npcs.MAN_3260, Npcs.MAN_3264, Npcs.MAN_3265,
        Npcs.MAN_3266, Npcs.MAN_3652, Npcs.MAN_6987, Npcs.MAN_6988, Npcs.MAN_6989,
        Npcs.MAN_7281)

MEN.forEach { man ->
    on_npc_option(npc = man, option = "talk-to") {
        player.queue { chat(this) }
    }
}

suspend fun chat(it: QueueTask) {
    it.chatPlayer("Hello, how's it going?", animation = 567)
    when (world.random(17)) {
        0 -> it.chatNpc("Not too bad thanks.", animation = 567)
        1 -> {
            it.chatNpc("I'm fine, how are you?", animation = 567)
            it.chatPlayer("Very well thank you.", animation = 567)
        }
        2 -> it.chatNpc("I think we need a new king. The one we've got isn't<br>very good.", animation = 589)
        3 -> it.chatNpc("Get out of my way, I'm in a hurry!", animation = 614)
        4 -> it.chatNpc("Do I know you? I'm in a hurry!", animation = 575)
        5 -> it.chatNpc("None of your business.", animation = 614)
        6 -> {
            it.chatNpc("Not too bad, but I'm a little worried about the increase<br>of goblins these days.", animation = 589)
            it.chatPlayer("Don't worry, I'll kill them.", animation = 567)
        }
        7 -> it.chatNpc("I'm busy right now.", animation = 588)
        8 -> {
            it.chatNpc("Who are you?", animation = 575)
            it.chatPlayer("I'm a bold adventurer.", animation = 567)
            it.chatNpc("Ah, a very noble profession.", animation = 567)
        }
        9 -> it.chatNpc("No I don't have any spare change.", animation = 575)
        10 -> {
            it.chatNpc("How can I help you?", animation = 588)
            when (it.options("Do you wish to trade?", "I'm in search of a quest.", "I'm in search of enemies to kill.")) {
                1 -> {
                    it.chatPlayer("Do you wish to trade?", animation = 588)
                    it.chatNpc("No, I have nothing I wish to get rid of. If you want to<br>do some trading, there are plenty of shops and market<br>stalls around though.", animation = 590)
                }
                2 -> {
                    it.chatPlayer("I'm in search of a quest.", animation = 567)
                    it.chatNpc("I'm sorry I can't help you there.", animation = 588)
                }
                3 -> {
                    it.chatPlayer("I'm in search of enemies to kill.", animation = 588)
                    it.chatNpc("I've heard there are many fearsome creatures that<br>dwell under the ground...", animation = 589)
                }

            }
        }
        11 -> it.chatNpc("I'm very well thank you.")
        12 -> {
            val npc = it.player.getInteractingNpc()
            it.chatNpc("Are you asking for a fight?", animation = 614)
            npc.attack(it.player)
        }
        13 -> it.chatNpc("I'm a little worried - I've heard there's lots of people<br>going about, killing citizens at random.", animation = 589)
        14 -> it.chatNpc("That is classified information.", animation = 588)
        15 -> it.chatNpc("Yo, wassup!", animation = 567)
        16 -> it.chatNpc("No, I don't want to buy anything!", animation = 614)
        17 -> it.chatNpc("Hello.", animation = 567)
    }
}