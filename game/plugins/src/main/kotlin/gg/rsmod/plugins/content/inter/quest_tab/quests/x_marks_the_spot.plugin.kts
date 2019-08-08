package gg.rsmod.plugins.content.inter.quest_tab.quests

/**
 * Thanks to kai for the quest Cooks assistant
 *
 * TO-DO
 * need to fix for when closing interface 277 starts chat
 * Need to fix chat from varp set 1 need to exit chat
 * Need to add in if player doesn't have item to replace from voes
 * Need to fix multi chat from one npc
 * Need to fix hiding or unspawning npc at lumbridge
 * Need to fix orb to do hot cold and not pull location
 * */

spawn_npc(Npcs.VEOS_8484, 3228, 3242, 0, 0, Direction.SOUTH)
spawn_npc(Npcs.VEOS_8484, 3054, 3245, 0, 0, Direction.NORTH)

on_npc_option(Npcs.VEOS_8484, option = "talk-to") {
    player.queue {
        if (player.getVarp(2111) == 0) { // NOT STARTED
            chatNpc("Hello there.")
            questStart(this)
        }
        if (player.getVarp(2111) <= 4) { // STARTED
            chatNpc("Hello there.")
            chatPlayer("Hello Veos.")
            aboutQuest(this)
        }
        if (player.getVarp(2111) == 5) { // CLUES FINISHED
            chatNpc("Hello there.")
            chatPlayer("Hello Veos.")
            questFinish(this)
        }
        if ((player.getVarp(2111) == 8) and (!player.isInterfaceVisible(277))) { // COMPLETED
            chatNpc("Hello there.")
            chatPlayer("Hello Veos.")
            afterQuestDialogue(this)
        }
    }
}

on_item_option(23067, "read") {
    player.openInterface(203, InterfaceDestination.MAIN_SCREEN)
    player.setComponentText(interfaceId = 203, component = 2, text = "Within the town of Lumbridge lives<br>a man named Bob. He walks<br>out of his door and takes 1 step<br>east, 7 steps north, 5 steps<br>west and 1 step south. Once<br>he arrives, he digs a hole and<br>buries his treasure.")
}
on_item_option(23068, "read") {
    player.openInterface(267, InterfaceDestination.MAIN_SCREEN)
}
on_item_option(item = 23069, option = "feel") {
    val tile = Tile(3108, 3264)
    val pos = player.tile
    val direction : String = when {
        pos.z > tile.z && pos.x - 1 > tile.x -> "south-west"
        pos.x < tile.x && pos.z > tile.z -> "south-east"
        pos.x > tile.x + 1 && pos.z < tile.z -> "north-west"
        pos.x < tile.x && pos.z < tile.z -> "north-east"
        pos.z < tile.z -> "north"
        pos.z > tile.z -> "south"
        pos.x < tile.x + 1 -> "east"
        pos.x > tile.x + 1 -> "west"
        else -> "feels close by"
    }
    player.message("The orb pulls towards the $direction.")
}
on_item_option(23070, "read") {
    player.openInterface(203, InterfaceDestination.MAIN_SCREEN)
    player.setComponentText(interfaceId = 203, component = 2, text = "The cipher reveals<br>where to dig next:<br><br>ESBZOPS QJH QFO")
}
on_item_option(23071, "open") {
    player.queue { chatPlayer("I don't think Veos would want me to open his treasure.<br>I should take it to him. If I remember right, it's docked<br>at the northernmost pier in Port Sarim.") }
}

suspend fun questStart(it: QueueTask) {
    when (it.options("Who are you?", "I'm looking for a quest.", "I have to go.")) {
        1 -> {
            it.chatPlayer("Who are you?")
            it.chatNpc("The name's Veos. I'm a treasure hunter from the<br>wonderous Kingdom of Great Kourend.")
            greatKourend(it)
            when (it.options("Can I help?", "Well good luck with it.")) {
                1 -> {
                    it.chatPlayer("Can I help?")
                    it.chatNpc("Hmmm. Maybe you can. You probably know this area<br>better than I do. You might be able to work the scroll<br>out. I'll be able to reward you if you help.")
                    when (it.options("Sounds good, what should I do?", "I'm good thanks.")) {
                        1 -> soundsGood(it)
                        2 -> goodLuck(it)
                    }
                }
                2 -> it.chatPlayer("Well good luck with it.")
            }
        }
        2 -> {
            it.chatPlayer("I'm looking for a quest.")
            it.chatNpc("Hmmm. Well now that you mention it, I could do with<br>some help. The name's Veos. I'm a treasure hunter<br>from the wonderous Kingdom of Great Kourend.")
            greatKourend(it)
            when (it.options("Sounds good, what should I do?", "I'm good thanks.")){
                1 -> soundsGood(it)
                2 -> goodLuck(it)
            }
        }
        3 -> it.chatPlayer("I have to go.")
    }
}

suspend fun soundsGood(it: QueueTask) {
    it.player.setVarp(2111, 1)
    it.chatPlayer("Sounds good, what should I do?")
    it.chatNpc("Take this scroll. It should lead you to the treasure I<br>seek. Once you've found it, meet me at my ship. It's<br>docked at the northernmost pier in Port Sarim.")
    it.player.inventory.add(Items.TREASURE_SCROLL, 1)
    it.itemMessageBox("Veos has given you a scroll.", item = 23067, amountOrZoom = 400)
    it.chatPlayer("Awesome. Anything else I should know?")
    it.chatNpc("You'll probably want a spade, I find they're pretty<br>much always needed when it comes to hunting treasure.<br>Check the general store if you don't have one. If you<br>need any extra help, just let me know.")
    when (it.options("Okay, thanks Veos.", "Tell me more about Great Kourend.")){
        1 -> {
                it.chatPlayer("Okay, thanks Veos.")
                it.chatNpc("Good luck.")
        }
        2 -> {
            tellmore(it)
            it.chatPlayer("Sounds like a fascinating place. Anyway, I'd better get<br>started with this treasure hunt.")
            it.chatNpc("Good luck.")
        }
    }
}

suspend fun greatKourend(it: QueueTask) {
    it.chatPlayer("Great Kourend? Where's that?")
    it.chatNpc("Across the sea to the far west. It is a truly magnificent<br>place.")
    it.chatPlayer("Interesting. So what brings you to Lumbridge?")
    it.chatNpc("I am here on a bit of a hunt. The hunt for treasure.<br>Back in my home of Great Kourend, I came across a<br>scroll. I believe it will lead me to something of great<br>value.")
    it.chatNpc("Alas, I've hit a bit of a blocker. The scroll has led me<br>here but as I don't really know the area, I'm not sure<br>what to do next.")
    return
}

suspend fun goodLuck(it: QueueTask) {
    it.chatPlayer("I'm good thanks.")
    it.chatNpc("Fair enough. I'll be here if you change your mind.")
    return
}

suspend fun tellmore(it: QueueTask) {
    it.chatPlayer("Tell me more about Great Kourend.")
    it.chatNpc("Great Kourend is a magnificent kingdom comprising of<br>five cities. They are the cities of Arceuus, Lovakengj,<br>Shayzien, Piscarilius and Hosidius. Each city is ruled<br>by one of the five houses of Kourend.")
    it.chatNpc("At one time, the kingdom as a whole was ruled over by<br>a king or queen that the five houses answered to.")
    it.chatNpc("However, since our last King died 20 years ago, the<br>kingdom has instead been ruled by an elected council.")
    return
}

suspend fun aboutQuest(it: QueueTask) {
    when (it.options("Tell me more about Great Kourend.", "Let's talk about my quest.", "Goodbye.")){
        1 -> {
            tellmore(it)
            when (it.options("Let's talk about my quest.", "Goodbye.")){
                1 -> {
                    it.chatPlayer("Let's talk about my quest.")
                    it.chatNpc("How's the treasure hunt going?")
                    when (it.options("I could do with some extra help.", "I'm still working on it.")){
                        1 -> extraHelp(it)
                        2 -> {
                            it.chatPlayer("I'm still working on it.")
                            it.chatNpc("Well keep at it. Once you've found the treasure, meet<br>me at my ship. It's docked at the northernmost pier in<br>Port Sarim.")
                        }
                    }
                }
                2 -> it.chatPlayer("Goodbye.")
            }
        }
        2 -> {
            it.chatPlayer("Let's talk about my quest.")
            it.chatNpc("How's the treasure hunt going?")
            when (it.options("I could do with some extra help.", "I'm still working on it.")){
                1 -> extraHelp(it)
                2 -> {
                    it.chatPlayer("I'm still working on it.")
                    it.chatNpc("Well keep at it. Once you've found the treasure, meet<br>me at my ship. It's docked at the northernmost pier in<br>Port Sarim.")
                }
            }
        }
        3 -> it.chatPlayer("Goodbye.")
    }
}

suspend fun extraHelp(it: QueueTask) {
    it.chatPlayer("I could do with some extra help.")
    if (it.player.inventory.contains(23067)) {
        it.itemMessageBox("You show the scroll to Veos.", item = 23070)
        it.chatNpc("This is the scroll I gave you. I don't know how to solve<br>it I'm afraid. Maybe look for someone called Bob?")
    }
    if (it.player.inventory.contains(23068)) {
        it.itemMessageBox("You show the scroll to Veos.", item = 23070)
        it.chatNpc("Looks like a map to me. I'd assume the X marks where<br>you need to go.")
    }
    if (it.player.inventory.contains(23069)) {
        it.itemMessageBox("You show the orb to Veos.", item = 23069)
        it.chatNpc("Interesting, these are quite rare. They work using<br>temperature. The closer you are, the hotter the orb.")
    }
    if (it.player.inventory.contains(23070)) {
        it.itemMessageBox("You show the scroll to Veos.", item = 23070)
        it.chatNpc("Ah a cipher, I've seen these before. Try shifting the<br>letters one to the left or right. That normally works.")
    }
}

suspend fun questFinish(it: QueueTask) {
    it.chatNpc("How's the treasure hunt going?")
    it.chatPlayer("I found the treasure!")
    it.chatNpc("Excellent. I'll take it off your hands now.")
    it.player.inventory.remove(Items.ANCIENT_CASKET, 1)
    it.itemMessageBox("You give Veos the Ancient Casket.", item = 23071)
    it.chatNpc("Brilliant. This is just what I was looking for. Thank you<br>so much for your help.")
    it.chatPlayer("No problem. So what is this treasure anyway?")
    it.chatNpc("Oh err... Nothing important. Just something that might<br>be of use to me back in Great Kourend.")
    it.chatPlayer("If you say so...")
    it.chatNpc("Anyway, as promised, a reward for you.")
    questCompleted(it)
}

suspend fun questCompleted(it: QueueTask) {
    it.player.setVarp(2111, 8)
    it.player.setVarp(101, it.player.getVarp(101) + 1)
    it.player.message("Congratulations! Quest complete!")
    it.player.openInterface(277, dest = InterfaceDestination.MAIN_SCREEN)
    it.player.setComponentText(interfaceId = 277, component = 1, text = "Congratulations!")
    it.player.setComponentText(interfaceId = 277, component = 2, text = "You have completed X Marks the Spot!")
    it.player.setComponentItem(interfaceId = 277, component = 3, item = 23071, amountOrZoom = 250)
    it.player.setComponentText(interfaceId = 277, component = 4, text = "")
    it.player.setComponentText(interfaceId = 277, component = 5, text = "")
    it.player.setComponentText(interfaceId = 277, component = 6, text = "1 Quest Point")
    it.player.setComponentText(interfaceId = 277, component = 7, text = "An Antique Lamp")
    it.player.setComponentText(interfaceId = 277, component = 8, text = "200 Coins")
    it.player.setComponentText(interfaceId = 277, component = 9, text = "A beginner clue scroll")
    it.player.setComponentText(interfaceId = 277, component = 10, text = "")
    it.player.setComponentText(interfaceId = 277, component = 11, text = "")
    it.player.setComponentText(interfaceId = 277, component = 12, text = "")
    it.player.setComponentText(interfaceId = 277, component = 13, text = "")
    it.player.setComponentText(interfaceId = 277, component = 14, text = "")
    it.player.inventory.add(Items.COINS_995, 200)
    it.player.inventory.add(Items.CLUE_SCROLL_BEGINNER, 1)
    it.player.inventory.add(Items.ANTIQUE_LAMP_23072, 1)//interface 134

    it.chatNpc("Anyway, I'd best be getting back to Great Kourend.<br>I'm sure I'll be back here before long though, there's<br>always more treasure to be found.")
    it.chatNpc("If you ever fancy visiting the kingdom, come find me<br>here. I'm more than happy to take you there. Consider<br>it an extra thank you for your help.")
    it.chatPlayer("Sounds great, thanks Veos.")
    }

suspend fun afterQuestDialogue(it: QueueTask) {
    when (it.options("Can you take me to Great Kourend?", "Tell me more about Great Kourend.", "Nothing.")){
        1 -> {
            it.chatPlayer("Can you take me to Great Kourend?")
            it.chatNpc("Unfortunately it seems that you're not quite ready to<br>visit Great Kourend yet. Come back when you are and<br>I'll happily take you there.")
        }
        2 -> {
            tellmore(it)
            when (it.options("That's great, can you take me there please?", "Goodbye.")){
                1 -> {
                    it.chatPlayer("That's great, can you take me there please?")
                    it.chatNpc("Unfortunately it seems that you're not quite ready to<br>visit Great Kourend yet. Come back when you are and<br>I'll happily take you there.")
                }
                2 -> it.chatPlayer("Goodbye.")
            }
        }
        3 -> it.chatPlayer("Nothing.")
    }
}

//FOR TESTING
on_command("q21") {
    player.setVarp(2111, 0)
    player.setVarp(101, player.getVarp(101)-1)
}