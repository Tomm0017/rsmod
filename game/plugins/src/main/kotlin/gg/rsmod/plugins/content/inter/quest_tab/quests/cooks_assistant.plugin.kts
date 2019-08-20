package gg.rsmod.plugins.content.inter.quest_tab.quests

spawn_npc(Npcs.COOK_4626, 3210, 3216, 0, 4, Direction.WEST)

var asked = 0
var takenItems = 0
var takenFlour = 0
var takenMilk = 0
var takenEgg = 0
var rangeUnlocked = 0

on_npc_option(4626, option = "talk-to") {
    player.queue {
        if (player.getVarp(29) == 0) { // NOT STARTED
            chatNpc("What am I to do?")
            askForQuest(this)
        }
        if (player.getVarp(29) == 1) { // STARTED
            checkItems(this)
        }
        if ((player.getVarp(29) == 2) and (!player.isInterfaceVisible(277))) { // COMPLETED
            afterQuestDialogue(this)
        }
    }
}

suspend fun askForQuest(it: QueueTask) {
    when (it.options("What's wrong?","Can you make me a cake?","You don't look very happy.","Nice hat!")) {
        1 -> whatIsWrong(it)
        2 -> {
            it.chatPlayer("You're a cook, why don't you bake me a cake?")
            it.chatNpc("*sniff* Don't talk to me about cakes...")
            whatIsWrong(it)
        }
        3 -> {
            it.chatPlayer("You don't look very happy.")
            it.chatNpc("No, I'm not. The world is caving in around me - I am<br><br>overcome by dark feelings of impending doom.")
            when (it.options("What's wrong?","I'd take the rest of the day off if I were you.")) {
                1 -> whatIsWrong(it)
                2 -> {
                    it.chatPlayer("I'd take the rest of the day off if I were you.")
                    it.chatNpc("No, that's the worst thing I could do. I'd get in terrible<br><br> trouble.")
                    it.chatPlayer("Well maybe you need to take a holiday...")
                    it.chatNpc("That would be nice, but the Duke doesn't allow holidays<br><br>for core staff.")
                    it.chatPlayer("Hmm, why not run away to the sea and start a new<br><br>life as a Pirate?")
                    it.chatNpc("My wife gets sea sick, and I have an irrational fear of<br><br>eyepatches. I don't see it working myself.")
                    it.chatPlayer("I'm afraid I've run out of ideas.")
                    it.chatNpc("I know I'm doomed.")
                    whatIsWrong(it)
                }
            }
        }
        4 -> {
            it.chatPlayer("Nice hat!")
            it.chatNpc("Err thank you. It's a pretty ordinary cooks hat really.")
            it.chatPlayer("Still, suits you. The trousers are pretty special too.")
            it.chatNpc("Its all standard cook's issue uniform....")
            it.chatPlayer("The whole hat, apron, stripey trousers ensemble - it<br><br>works. It make you look like a real cook.")
            it.chatNpc("I am a real cook! I haven't got time to be chatting<br><br>about Culinary Fashion. I am in desperate need of help!")
            whatIsWrong(it)
        }
    }
}

suspend fun whatIsWrong(it: QueueTask) {
    it.chatPlayer("What's wrong?")
    it.chatNpc("Oh dear, oh dear, oh dear, I'm in a terrible terrible mess! It's the Duke's birthday today, and I should be making him a lovely big birthday cake.")
    it.chatNpc("I've forgotten to buy the ingredients. I'll never get them in time now. He'll sack me! What will I do? I have four children and a goat to look after. Would you help me? Please?")
    when (it.options("I'm always happy to help a cook in distress.","I can't right now, Maybe later.")) {
        1 -> {
            it.chatPlayer("Yes, I'll help you.")
            it.player.setVarp(29, 1)
            it.chatNpc("Oh thank you, thank you. I need milk, an egg and<br><br>flour. I'd be very grateful if you can get them for me.")
            it.chatPlayer("So where do I find these ingredients then?")
            whereToFind(it)
        }
        2 -> {
            it.chatPlayer("No, I don't feel like it. Maybe later.")
            it.chatNpc("Fine. I always knew you Adventurer types were callous<br><br>beasts. Go on your merry way!")
        }
    }
}

suspend fun whereToFind(it: QueueTask) {
    if (asked == 0) {
        when (it.options("Where do I find some flour?", "How about milk?", "And eggs? Where are they found?", "Actually, I know where to find this stuff.")) {
            1 -> flour(it)
            2 -> milk(it)
            3 -> egg(it)
            4 -> {
                it.chatPlayer("Actually, I know where to find this stuff.")
                asked = 0
                it.player.interfaces.close(162, child = CHATBOX_CHILD)
                it.player.resetInteractions()
            }
        }
    }
    if (asked >= 1) {
        when (it.options("Where do I find some flour?", "How about milk?", "And eggs? Where are they found?", "I've got all the information I need. Thanks.")) {
            1 -> flour(it)
            2 -> milk(it)
            3 -> egg(it)
            4 -> {
                it.chatPlayer("I've got all the information I need. Thanks.")
                asked = 0
                it.player.interfaces.close(162, child = CHATBOX_CHILD)
                it.player.resetInteractions()
            }
        }
    }
}

suspend fun flour (it: QueueTask) {
    asked++
    it.chatNpc("There is a cattle field on the other side of the river,<br><br>just across the road from the Groats' Farm.")
    it.chatNpc("Talk to Gillie Groats, she looks after the Dairy cows - she'll tell you everything you need to know about milking cows!")
    it.chatNpc("You'll need an empty bucket for the milk itself. The<br>general store just north of the castle will sell you one<br>for a couple of coins.")
    whereToFind(it)
}
suspend fun milk (it: QueueTask) {
    asked++
    it.chatNpc("There is a Mill fairly close, go North and then West. Mill Lane Mill is just off the road to Draynor. I usually get my flour from there.")
    it.chatNpc("Talk to Millie, she'll help, she's a lovely girl and a fine<br>Miller. Make sure you take a pot with you for the flour<br>though, there should be one on the table in here.")
    whereToFind(it)
}
suspend fun egg (it: QueueTask) {
    asked++
    it.chatNpc("I normally get my eggs from the Groats' Farm, on the<br><br>other side of the river.")
    it.chatNpc("But any chicken should lay eggs.")
    whereToFind(it)
}

suspend fun checkItems(it: QueueTask) {
    it.chatNpc("How are you getting on with finding the ingredients?")
    if (it.player.inventory.contains(Items.POT_OF_FLOUR) and (takenFlour == 0)) {
        it.player.inventory.remove(Items.POT_OF_FLOUR)
        it.chatPlayer("Here's a pot of flour.")
        takenFlour++
        takenItems++
    }
    if (it.player.inventory.contains(Items.BUCKET_OF_MILK) and (takenMilk == 0)) {
        it.player.inventory.remove(Items.BUCKET_OF_MILK)
        it.chatPlayer("Here's a bucket of milk.")
        takenMilk++
        takenItems++
    }
    if (it.player.inventory.contains(Items.EGG) and (takenEgg == 0)) {
        it.player.inventory.remove(Items.EGG)
        it.chatPlayer("Here's a fresh egg.")
        takenEgg++
        takenItems++
    }

    if (takenItems == 0) {
        it.chatPlayer("I haven't got any of them yet, I'm still looking.")
        it.chatNpc("Please get the ingredients quickly. I'm running out of<br><br>time! The Duke will throw me into the streets!")
        it.messageBox("You still need to get:<br>A bucket of milk. A pot of flour. An egg.")
        aftercheck(it)
    }

    if (takenItems == 1) {
        it.chatNpc("Thanks for the ingredients you have got so far, please get the rest quickly. I'm running out of time! The Duke will throw me into the streets!")
        if (takenFlour == 1) { it.messageBox("You still need to get:<br>A bucket of milk. An egg.") }
        if (takenMilk == 1) { it.messageBox("You still need to get:<br>A pot of flour. An egg.") }
        if (takenEgg == 1) { it.messageBox("You still need to get:<br>A bucket of milk. A pot of flour.") }
        aftercheck(it)
    }

    if (takenItems == 2) {
        it.chatNpc("Thanks for the ingredients you have got so far, please get the rest quickly. I'm running out of time! The Duke will throw me into the streets!")
        if (takenFlour == 1 && takenMilk == 1) { it.messageBox("You still need to get:<br>An egg.") }
        if (takenFlour == 1 && takenEgg == 1) { it.messageBox("You still need to get:<br>A bucket of milk.") }
        if (takenMilk == 1 && takenFlour == 1) { it.messageBox("You still need to get:<br>An egg.") }
        if (takenMilk == 1 && takenEgg == 1) { it.messageBox("You still need to get:<br>A pot of flour.") }
        if (takenEgg == 1 && takenFlour == 1) { it.messageBox("You still need to get:<br>A bucket of milk.") }
        if (takenEgg == 1 && takenMilk == 1) { it.messageBox("You still need to get:<br>A pot of flour.") }
        aftercheck(it)
    }

    if (takenItems == 3) {
        it.chatNpc("You've brought me everything I need! I am saved!<br><br>Thank you!")
        it.chatPlayer("So do I get to go to the Duke's Party?")
        it.chatNpc("I'm afraid not, only the big cheeses get to dine with the<br><br>Duke.")
        it.chatPlayer("Well, maybe one day I'll be important enough to sit on<br><br>the Duke's table.")
        it.chatNpc("Maybe, but I won't be holding my breath.")
        questCompleted(it)
    }
}

suspend fun aftercheck (it: QueueTask) {
    when (it.options("I'll get right on it.", "Can you remind me how to find these things again?")) {
        1 -> it.chatPlayer("I'll get right on it.")
        2 -> {
            it.chatPlayer("So where do I find these ingredients then?")
            whereToFind(it)
        }
    }
}

suspend fun questCompleted(it: QueueTask) {
    it.player.setVarp(29, 2)
    it.player.setVarp(101, it.player.getVarp(101)+1)
    it.player.message("Congratulations! Quest complete!")
    it.player.openInterface(277, dest = InterfaceDestination.MAIN_SCREEN)
    it.player.setComponentText(277, 1, "Congratulations!")
    it.player.setComponentText(277, 2, "You have completed the Cook's Assistant Quest!")
    it.player.setComponentItem(277, 3, 1891, 185)
    it.player.setComponentText(277, 4, "Quest Points: ${it.player.getVarp(101)}")
    it.player.setComponentText(277, 5, "")
    it.player.setComponentText(277, 7, "You are awarded:")
    it.player.setComponentText(277, 8, "1 Quest Point")
    it.player.setComponentText(277, 9, "300 Cooking XP")
    it.player.setComponentText(277, 10, "")
    it.player.setComponentText(277, 11, "")
    it.player.setComponentText(277, 12, "")
    it.player.setComponentText(277, 13, "")
    it.player.setComponentText(277, 14, "")
    it.player.addXp(Skills.COOKING, 300.0)
    rangeUnlocked++
}

suspend fun afterQuestDialogue(it: QueueTask) {
    it.chatNpc("Hello friend, how is the adventuring going?")
    when (it.options("I am getting strong and mighty.","I keep on dying","Can I use your range?")) {
        1 -> {
            it.chatPlayer("I am getting strong and mighty. Grrr")
            it.chatNpc("Glad to hear it.")
        }
        2 -> {
            it.chatPlayer("I keep on dying.")
            it.chatNpc("Ah well, at least you keep coming back to life!")
        }
        3 -> {
            it.chatPlayer("Can I use your range?")
            it.chatNpc("Go ahead - it's a very good range. It's easier to use<br><br>than most other ranges.")
            it.chatNpc("Its called the Cook-o-matic 100. It uses a combination of<br><br>state of the art temperature regulation and magic.")
            it.chatPlayer("Will it mean my food will burn less often?")
            it.chatNpc("Well, that's what the salesman told us anyway...")
            it.chatPlayer("Thanks?")
        }
    }
}

on_obj_option(114, option = "cook") {
    player.queue {
        if (rangeUnlocked == 0) {
            chatNpc("Hey, who said you could use that?", 4626)
        } else {
            messageBox("You haven't got anything to cook.") //WIP
        }
    }
}

//FOR TESTING
on_command("q2") {
    player.setVarp(29, 0)
    player.setVarp(101, player.getVarp(101)-1)
    player.inventory.add(Items.EGG)
    player.inventory.add(Items.POT_OF_FLOUR)
    player.inventory.add(Items.BUCKET_OF_MILK)
    asked = 0
    takenItems = 0
    takenFlour = 0
    takenMilk = 0
    takenEgg = 0
    rangeUnlocked = 0
}