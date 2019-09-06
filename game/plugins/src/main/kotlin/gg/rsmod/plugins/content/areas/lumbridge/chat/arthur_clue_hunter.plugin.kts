package gg.rsmod.plugins.content.areas.lumbridge.chat

spawn_npc(Npcs.ARTHUR_THE_CLUE_HUNTER, 3229, 3236, 0, 0, Direction.NORTH)

val YELL_DELAY = TimerKey()

on_npc_option(npc = Npcs.ARTHUR_THE_CLUE_HUNTER, option = "talk-to") {
    player.queue {
        chatNpc("What can I do for you?")
        dialog()
    }
}

on_npc_spawn(npc = Npcs.ARTHUR_THE_CLUE_HUNTER) {
    val npc = npc
    npc.timers[YELL_DELAY] = world.random(25..30)
}

on_timer(YELL_DELAY) {
    val npc = npc
    when (world.random(4)) {
        0 -> {
            npc.animate(4275)
            npc.forceChat("Why did I not think of that before.")
        }
        1 -> {
            npc.animate(2109)
            npc.forceChat("I'm going to be rich!")
        }
        2 -> {
            npc.animate(2113)
            npc.forceChat("Hmm... What could this mean?")
        }
        3 -> {
            npc.animate(4278)
            npc.forceChat("Why is this so hard...")
        }
        4 -> {
            npc.animate(4276)
            npc.forceChat("I've got it!")
        }
    }
    npc.timers[YELL_DELAY] = world.random(25..30)
}

suspend fun QueueTask.dialog() {
    when (options("Ask about beginner clues.", "Ask about more advances clues.", "Ask about the types of clues.", "Toggle gaining vesseled clue scrolls.", "Goodbye.")) {
        1 ->{
            chatPlayer("What are beginner clues?")
            messageBox("Arthur's eyes light up.")
            chatNpc("Beginner clues are the lowest tier of clue scroll and as<br><br>such only require a couple steps to solve.")
            chatNpc("Beginner clues are gained through various activities.<br><br>Those activities are as follows.")
            chatNpc("Slaying monsters for a chance at a clue beging dropped. I hear goblins are sneaky, maybe they have some clue scrolls on them ripe for the taking.")
            chatNpc("Maybe someone left one in the cow field, who knows<br><br>where you could obtain them.")
            chatNpc("Beginner clues have also been known to be left in<br>geodes while mining, as well as somethings in bottles that<br>you may fish up from time to time and from nests<br>which fall out of trees.")
            chatNpc("Although this is a much rarer occurrence.")
            chatNpc("Unlike more advances clues, you should not expect to<br>get very valuable items from completing a beginner<br>clue.")
            dialog()
        }
        2 -> {
            chatPlayer("What are more advanced clues?")
            chatNpc("Advanced clues have tiers between easy and master.")
            chatNpc("Unfortunately these are members only but I will explain<br><br>them to you.")
            chatNpc("You can gain advances clues the same way you would<br>get a beginner clue. But to get higher tier scrolls you<br>will need to defeat more powerful foes.")
            chatNpc("The chance to get a higher tier clue from geodes,<br>bottles and nests is lower the higher the tier. But if you<br>chop down highter-level trees you will have a higher<br>chance of receiving a nest which contains a clue. Same")
            chatNpc("with fishing and mining.")
            chatNpc("The only exception to this is master clue scrolls.")
            chatNpc("Master clues can only be obtained as a rare chance when opening your casket or by handing in one of every type of clue, apart from beginner, to Watson who resides in Hosidius.")
            chatNpc("But with more advances tiers of clue, come better loot.")
            dialog()
        }
        3 -> clueTypes()
        4 -> {
            if (player.getVarbit(9365) == 0) {
                chatPlayer("Hey Arthur, can I stop receiving clues from vessels?<br><br>They're clogging up my backpack!")
                chatNpc("Vessel containers include mining geodes, fishing bottles<br>and clue nests. Are you sure you want to stop<br>receiving them?")
                when (options("Yes please, I want to stop receiving vesseled containers.", "Nevermind.")) {
                    1 -> {
                        chatPlayer("Yes please, I want to stop receiving vesseled containers.")
                        player.setVarbit(9365, 1)
                        chatNpc("Very well, you'll no longer find clues from mining,<br><br>fishing and woodcutting.")
                        chatNpc("Is there anything else I can help you with?")
                        dialog()
                    }
                    2 -> dialog()
                }
            }
            if (player.getVarbit(9365) == 1) {
                chatPlayer("Hey Arthur, can I start receiving clues from vessels?")
                chatNpc("Vessel containers include mining geodes, fishing bottles<br>and clue nests. Are you sure you want to start<br>receiving them, again?")
                when (options("Yes please, I want to start receiving vesseled containers.", "Nevermind.")) {
                    1 -> {
                        chatPlayer("Yes please, I want to start receiving vesseled containers.")
                        player.setVarbit(9365, 0)
                        chatNpc("Very well, you'll now find clues from mining, fishing<br><br> and woodcutting.")
                        chatNpc("Is there anything else I can help you with?")
                        dialog()
                    }
                    2 -> dialog()
                }
            }
        }
        5 -> {
            chatPlayer("Goodbye.")
            chatNpc("So long and happy clue hunting!")
        }

    }
}

suspend fun QueueTask.clueTypes() {
    when (options("Anagrams.", "Challenge scrolls.", "Cipher clues.", "Coordinate clues.", "More.")) {
        1 -> {
            chatNpc("This type of clue has you talk to someone around<br>Gielinore. Although the clue does not tell you exactly who<br> to talk to.")
            chatNpc("You will see a couple of words that may or may not<br>make sense together. You will have to rearrange the<br>letters to make the name of the person you need to talk<br>to.")
            clueTypes()
        }
        2 -> {
            chatNpc("This type of clue has you work out a logical problem<br>for the person you need to talk to. This type of<br>challenge only appears in clue tiers medium, hard and<br>elite.")
            chatNpc("There are also challenges that will involve you gathering<br><br>an item or crafting an item for a certain person.")
            chatNpc("Unfortunately, the only type of challenge that you will<br><br>be giving is a skill challenge.")
            clueTypes()
        }
        3 -> {
            chatNpc("This type of clues has you talk to someone around<br>Gielinor. Although the clue does not tell you exactly who<br>to talk to. This type of challenge only appears in clue<br>tiers medium, hard and elite.")
            chatNpc("A cipher is a string of letters that have been scrambled<br><br>using the shift cipher method.")
            chatNpc("An example of this would be ZCZL which, shifter forward by one, would be Adam.")
            chatNpc("Unfortunately, this type of clue is members only.")
            clueTypes()
        }
        4 -> {
            chatNpc("This type of clue will have you use a sextant, chart and<br>a watch to find out the place you will need to dig. This<br>type of challenge only appears in clue tiers medium and<br>above.")
            chatNpc("After you find the place you need to dig, you may find<br><br>a casket or another scroll.")
            chatNpc("Unfortunately, this type of clue is members only.")
            clueTypes()
        }
        5 -> moreClues()
    }
}

suspend fun QueueTask.moreClues() {
    when (options("Cryptic clues.", "Emote clues.", "Hot Cold clues.", "Puzzle clues.", "More.")) {
        1 -> {
            chatNpc("This type of clue be as simple as talk to the<br>specified person or searching a crate in specified<br>location.")
            chatNpc("But the higher the tier of clue, the more steps you will<br>have to do beforehand. An example of this would be<br>needing to find a key before opening a chest.")
            chatNpc("Unfortunately, you will only be given the easier ones in<br><br>beginner clues.")
            moreClues()
        }
        2 -> {
            chatNpc("This type of clue will give you a location that you will<br><br>need to visit, along with some items you will need.")
            chatNpc("At this location you will need to perform an emote, as<br><br>well as wear the items that you had to bring along.")
            chatNpc("Some clues may have you wear nothing at all.")
            chatNpc("But please don't do that in public!")
            chatNpc("This type of challenge only appears in clue tiers<br><br>medium and above.")
            moreClues()
        }
        3 -> {
            chatNpc("Depending on the tier of clue, you will need to obtain a<br>strange device from either Reldo, located in Varrock, or<br>Jorral, located south of Tree Gnome Stronghold.")
            chatNpc("You will then need to use this device to find the location<br><br>you will need to dig.")
            chatNpc("You will only need to talk to Reldo for beginner clues.")
            moreClues()
        }
        4 -> {
            chatNpc("There are many different puzzles that you can come<br>across while doing clues. These range from light boxes<br>to puzzle boxes.")
            chatNpc("Puzzle boxes are puzzles that will have you complete a<br>slider puzzle that, when complete, will look like an image<br>in game.")
            chatNpc("Light boxes are a puzzle which contains a 5 by 5 grid<br><br>of lights with some lights on and some lights off.")
            chatNpc("The puzzle will have you click the buttons below to turn<br>on or off some of the lights with the end goal being,<br>having all the lights turned on.")
            chatNpc("Unfortunately, this type of challenge is members only.")
            moreClues()
        }
        5 -> clueEnd()
    }
}

suspend fun QueueTask.clueEnd() {
    when (options("Map clues.", "Fairy ring clues.", "More.", "Ask about something else.")) {
        1 -> {
            chatNpc("This type of clue will show you a map of an area.")
            chatNpc("The map will show you where you will need to dig, or<br><br>what you will need to search to get the next clue.")
            clueEnd()
        }
        2 -> {
            chatNpc("This type of clue will show you which fairy ring code<br><br>you will need to use to get to the right place to dig.")
            chatNpc("You will then need to use the number to figure out<br>you will need to dig after arriving to your<br>destination")
            chatNpc("Unfortunately, you need to be a member to be able to<br><br>do this clue.")
            clueEnd()
        }
        3 -> clueTypes()
        4 -> dialog()
    }
}
