package gg.rsmod.plugins.content.areas.lumbridge.chat

spawn_npc(Npcs.GEE, 3217,3223,0, 19, Direction.NORTH)
spawn_npc(Npcs.GEE, 3243, 3265, 0, 22)

on_npc_option(npc = Npcs.GEE, option = "talk-to") {
    player.queue { dialog(this) }
}

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Hello there, can I help you?", animation = 591)
    when (world.random(3)) {
        0 -> {
            when (it.options("Where am I?", "How are you today?", "Are there any quests I can do here?", "Your shoe lace is untied.")) {
                1 -> {
                    it.chatPlayer("Where am I?")
                    it.chatNpc("This is the town of Lumbridge my friend.")
                    when (it.options("How are you today?", "Do you know of any quests I can do?", "Your shoe lace is untied.")) {
                        1 -> howareyou(it)
                        2 -> anyquests(it)
                        3 -> shoelace(it)
                    }
                }
                2 -> howareyou(it)
                3 -> anyquests(it)
                4 -> shoelace(it)
            }
        }
        1 -> {
            when (it.options("What's up?", "Are there any quests I can do here?", "Can I buy your stick?")) {
                1 -> {
                    it.chatPlayer("What's up?")
                    it.chatNpc("I assume the sky is up..")
                    it.chatPlayer("You assume?")
                    it.chatNpc("Yeah, unfortunately I don't seem to be able to look up.")
                }
                2 -> anyquests(it)
                3 -> stick(it)
            }
        }
        2 -> {
            when (it.options("Do you have anything of value which I can have?", "Are there any quests I can do here?", "Can I buy your stick?")) {
                1 -> {
                    it.chatPlayer("Do you have anything of value which I can have?")
                    it.chatNpc("Are you asking for free stuff?")
                    it.chatPlayer("Well... er... yes.")
                    it.chatNpc("No I do not have anything I can give you. If I did<br>have anything of value I wouldn't want to give it away.")
                }
                2 -> anyquests(it)
                3 -> stick(it)
            }
        }
        3 -> {
            when (it.options("Where am I?", "How are you today?", "Are there any quests I can do here?", "Where can I get a haircut like yours?")) {
                1 -> {
                    it.chatPlayer("Where am I?")
                    it.chatNpc("This is the town of Lumbridge my friend.")
                    when (it.options("How are you today?", "Do you know of any quests I can do?", "Your shoe lace is untied.")) {
                        1 -> howareyou(it)
                        2 -> anyquests(it)
                        3 -> shoelace(it)
                    }
                }
                2 -> howareyou(it)
                3 -> anyquests(it)
                4 -> {
                    it.chatPlayer("Where can I get a haircut like yours?")
                    it.chatNpc("Yes, it does look like you need a hairdresser.")
                    it.chatPlayer("Oh thanks!")
                    it.chatNpc("No problem. The hairdresser in Falador will probably be<br>able to sort you out.")
                    it.chatNpc("The Lumbridge general store sells useful maps if you<br>don't know the way.")
                }
            }
        }
    }
}

suspend fun howareyou(it: QueueTask) {
    it.chatPlayer("How are you today?")
    it.chatNpc("Aye, not too bad thank you. Lovely weather in Gielinor<br>this fine day.")
    it.chatPlayer("Weather?")
    it.chatNpc("Yes weather, you know.")
    it.chatNpc("The state or condition of the atmosphere at a time and<br>place, with respect to variables such as temperature,<br>moisture, wind velocity, and barometric pressure.")
    it.chatPlayer("...")
    it.chatNpc("Not just a pretty face eh? Ha ha ha.")
}

suspend fun anyquests(it: QueueTask) {
    it.chatPlayer("Do you know of any quests I can do?")
    it.chatNpc("What kind of quest are you looking for?")
    when (it.options("I fancy a bit of a fight, anything dangerous?", "Something easy please, I'm new here.", "I'm a thinker rather than fighter, anything skill oriented?", "I want to do all kinds of things, do you know of anything like that?", "Maybe another time.")) {
        1 -> {
            it.chatPlayer("I fancy a bit of a fight, anything dangerous?")
            it.chatNpc("Hmm.. dangerous you say? What sort of creatures are<br>you looking to fight?")
            when (it.options("Big scary demons!", "Vampyres!", "Small.. something small would be good.", "Maybe another time.")) {
                1 -> {
                    it.chatPlayer("Big scary demons!")
                    it.chatNpc("You are a brave soul indeed.")
                    it.chatNpc("Now that you mention it, I heard a rumour about a<br>gypsy in Varrock who is rambling about some kind of<br>greater evil.. sounds demon-like if you ask me.")
                    it.chatNpc("Perhaps you could check it out if you are as brave as<br>you say?")
                    it.chatPlayer("Thanks for the tip, perhaps I will.")
                }
                2 -> {
                    it.chatPlayer("Vampyres!")
                    it.chatNpc("Ha ha. I personally don't believe in such things.<br>However, there is a man in Draynor Village who has<br>been scaring the village folk with stories of vampyres.")
                    it.chatNpc("He's named Morgan and can be found in one of the<br>village houses. Perhaps you could see what the matter<br>is?")
                    it.chatPlayer("Thanks for the tip.")
                }
                3 -> {
                    it.chatPlayer("Small.. something small would be good.")
                    it.chatNpc("Small? Small isn't really that dangerous though is it?")
                    it.chatPlayer("Yes it can be! There could be anything from an evil<br>chicken to a poisonous spider. They attack in numbers<br>you know!")
                    it.chatNpc("Yes ok, point taken. Speaking of small monsters, I hear<br>old Wizard Mizgog in the wizards' tower has just had<br>all his beads taken by a gang of mischievous imps.")
                    it.chatNpc("Sounds like it could be a quest for you?")
                    it.chatPlayer("Thanks for your help.")
                }
                4 -> it.chatPlayer("Maybe another time.")
            }
        }
        2 -> {
            it.chatPlayer("Something easy please, I'm new here.")
            it.chatNpc("I can tell you about plenty of small easy tasks.")
            it.chatNpc("The Lumbridge cook has been having problems, the<br>Duke is confused over some strange rocks and on top<br>of all that, poor lad Romeo in Varrock has girlfriend<br>problems.")
            when (it.options("The Lumbridge cook.", "The Duke's strange stones.", "Romeo and his girlfriend.", "Maybe another time.", title = "Tell me about..")) {
                1 -> {
                    it.chatPlayer("Tell me about the Lumbridge cook.")
                    it.chatNpc("It's funny really, the cook would forget his head if it<br>wasn't screwed on. This time he forgot to get<br>ingredients for the Duke's birthday cake.")
                    it.chatNpc("Perhaps you could help him? You will probably find him<br>in the Lumbridge Castle kitchen.")
                    it.chatPlayer("Thank you. I shall go speak with him.")
                }
                2 -> {
                    it.chatPlayer("Tell me about the Duke's strange stones.")
                    it.chatNpc("Well the Duke of Lumbridge has found a strange stone<br>that no one seems to understand. Perhaps you could<br>help him? You can probably find him upstairs in<br>Lumbridge Castle.")
                    it.chatPlayer("Sounds mysterious. I may just do that. Thanks.")
                }
                3 -> {
                    it.chatPlayer("Tell me about Romeo and his girlfriend please.")
                    it.chatNpc("Romeo in Varrock needs help with finding his beloved<br>Juliet, you may be able to help him out.")
                    it.chatNpc("Unless of course you manage to find Juliet first in<br>which case she has probably lost Romeo.")
                    it.chatPlayer("Right, ok. Romeo is in Varrock?")
                    it.chatNpc("Yes you can't miss him, he's wandering aimlessly in the<br>square.")
                }
                4 -> it.chatPlayer("Maybe another time.")
            }
        }
        3 -> {
            it.chatPlayer("I'm a thinker rather than fighter, anything skill<br>orientated?")
            it.chatNpc("Skills play a big part when you want to progress in<br>knowledge throughout Gielinor. I know of a few skill-<br>related quests that can get you started.")
            it.chatNpc("You may be able to help out Fred the farmer who is in<br>need of someones crafting expertise.")
            it.chatNpc("Or, there's always Doric the dwarf who needs an<br>errand running for him?")
            when (it.options("Fred the farmer.", "Doric the dwarf.", "Maybe another time.", title = "Tell me about..")) {
                1 -> {
                    it.chatPlayer("Tell me about Fred the farmer please.")
                    it.chatNpc("You can find Fred next to the field of sheep in<br>Lumbridge. Perhaps you should go and speak with him.")
                    it.chatPlayer("Thanks, maybe I will.")
                }
                2 -> {
                    it.chatPlayer("Tell me about Doric the dwarf.")
                    it.chatNpc("Doric the dwarf is located north of Falador. He might<br>be able to help you with smithing. You should speak to<br>him. He may let you use his anvils.")
                    it.chatPlayer("Thanks for the tip.")
                }
                3 -> it.chatPlayer("Maybe another time.")
            }


        }
        4 -> {
            it.chatPlayer("I want to do all kinds of things, do you know of<br>anything like that?")
            it.chatNpc("Of course I do. Gielinor is a huge place you know, now<br>let me think...")
            it.chatNpc("Hetty the witch in Rimmington might be able to offer<br>help in the ways of magical abilities..")
            it.chatNpc("Also, pirates are currently docked in Port Sarim,<br>Where pirates are, treasure is never far away...")
            it.chatNpc("Or you could go help out Ernest who got lost in<br>Draynor Manor, spooky place that.")
            when (it.options("Hetty the Witch.", "Pirate's treasure.", "Ernest and Draynor Manor.", "Maybe another time.", title = "Tell me about..")) {
                1 -> {
                    it.chatPlayer("Tell me about Hetty the witch.")
                    it.chatNpc("Hetty the witch can be found in Rimmington, south of<br>Falador. She's currently working on some new potions.<br>Perhaps you could give her a hand? She might be able<br>to offer help with your magical abilities.")
                    it.chatPlayer("Ok thanks, let's hope she doesn't turn me into a potato<br>or something..")
                }
                2 -> {
                    it.chatPlayer("Tell me about Pirate's Treasure.")
                    it.chatNpc("RedBeard Frank in Port Sarim's bar, the Rusty<br>Anchor might be able to tell you about the rumored<br>treasure that is buried somewhere in Gielinor.")
                    it.chatPlayer("Sounds adventurous, I may have to check that out.<br>Thank you.")
                }
                3 -> {
                    it.chatPlayer("Tell me about Ernest please.")
                    it.chatNpc("The best place to start would be at the gate to<br>Draynor Manor. There you will find Veronica who will<br>be able to tell you more.")
                    it.chatNpc("I suggest you tread carefully in that place; it's haunted.")
                    it.chatPlayer("Sounds like fun. I've never been to a Haunted Manor<br>before.")
                }
                4 -> it.chatPlayer("Maybe another time.")
            }
        }
        5 -> it.chatPlayer("Maybe another time.")
    }
}

suspend fun stick(it: QueueTask) {
    it.chatPlayer("Can I buy your stick?")
    it.chatNpc("It's not a stick! I'll have you know it's a very powerful<br>staff!")
    it.chatPlayer("Really? Show me what it can do!")
    it.chatNpc("Um..It's a bit low on power at the moment..")
    it.chatPlayer("It's a stick isn't it?")
    it.chatNpc("...Ok it's a stick.. But only while I save up for a staff.<br>Zaff in Varrock square sells them in his shop.")
    it.chatPlayer("Well good luck with that.")
}

suspend fun shoelace(it: QueueTask) {
    it.chatPlayer("Your shoe lace is untied.")
    it.chatNpc("No it's not!")
    it.chatPlayer("No you're right. I have nothing to back that up.")
    it.chatNpc("Fool! Leave me alone!")
}