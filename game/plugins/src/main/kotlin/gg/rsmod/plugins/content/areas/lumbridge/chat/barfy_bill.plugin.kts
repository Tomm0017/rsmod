package gg.rsmod.plugins.content.areas.lumbridge.chat

spawn_npc(npc = Npcs.BARFY_BILL, x = 3243, z = 3237, walkRadius = 3, height = 0)

on_npc_option(npc = Npcs.BARFY_BILL, option = "talk-to") {
    player.queue { dialog() }
}

suspend fun QueueTask.dialog() {
    chatPlayer("Hello there.", animation = 588)
    chatNpc("Oh! Hello there.", animation = 588)
    when (options("Who are you?", "Can you teach me about Canoeing?")) {
        1 -> {
            chatPlayer("Who are you?", animation = 588)
            chatNpc("My name is Ex Sea Captain Barfy Bill.", animation = 588)
            chatPlayer("Ex sea captain?", animation = 554)
            chatNpc("Yeah, I bought a lovely ship and was planning to make<br>a fortune running her as a merchant vessel.", animation = 611)
            chatPlayer("Why are you not still sailing?", animation = 554)
            chatNpc("Chronic sea sickness. My first, and only, voyage was<br>spent dry heaving over the rails.", animation = 611)
            chatNpc("If I had known about the sea sickness I could have<br>saved myself a lot of money.", animation = 589)
            chatPlayer("What are you up to now then?", animation = 575)
            chatNpc("Well my ship had a little fire related problem.<br>Fortunately it was well insured.", animation = 593)
            chatNpc("Anyway, I don't have to work anymore so I've taken to<br>canoeing on the river.", animation = 589)
            chatNpc("I don't get river sick!", animation = 567)
            chatNpc("Would you like to know how to make a canoe?", animation = 554)
            when (options("Yes", "No")) {
                1 -> teach()
                2 -> chatPlayer("No thanks, not right now.", animation = 588)
            }
        }
        2 -> teach()
    }
}

suspend fun QueueTask.teach() {
    if (player.getSkills().getCurrentLevel(Skills.WOODCUTTING) < 12) {
        chatPlayer("Could you teach me about canoes?", animation = 554)
        chatNpc("Well, you don't look like you have the skill to make a<br>canoe.", animation = 589)
        chatNpc("You need to have at least level 12 woodcutting.", animation = 588) //TODO
        chatNpc("Once you are able to make a canoe it makes travel<br>along the river much quicker!", animation = 589)
    } else {
        chatNpc("It's really quite simple. Just walk down to that tree on<br>the bank and chop it down.")
        chatNpc("When you have done that you can shape the log<br>further with your axe to make a canoe.")
        if (player.getSkills().getCurrentLevel(Skills.WOODCUTTING) > 57) {
            chatNpc("Hoo! You look like you know which end of an axe is<br>which!")
            chatNpc("You can easily build one of those Wakas. Be careful if<br>you travel into the Wilderness though.")
            chatNpc("I've heard tell of great evil in that blasted wasteland.")
            chatPlayer("Thanks for the warning Bill.")
        }
    }
}
