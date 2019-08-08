package gg.rsmod.plugins.content.inter.quest_tab.quests

/**
 * Thanks to kai for the quest Cooks assistant
 * Thanks to bmyte for loads of help making things work and the info
 * */

var ballsNeeded = 20

on_obj_option(12982, "climb-over") {
    val stile = player.getInteractingGameObj()
    val endTile: Tile
    val directionAngle: Int
    val southOfStile = player.tile.z < stile.tile.z

    if (southOfStile) {
        endTile = stile.tile.step(Direction.NORTH, 2)
        directionAngle = Direction.NORTH.angle
    } else {
        endTile = stile.tile.step(Direction.SOUTH, 1)
        directionAngle = Direction.SOUTH.angle
    }
    val movement = ForcedMovement.of(player.tile, endTile, clientDuration1 = 33, clientDuration2 = 60, directionAngle = directionAngle)
    player.jumpStile(movement)
}

fun Player.jumpStile(movement: ForcedMovement) {
    queue {
        animate(839)
        forceMove(this, movement)
    }
}

spawn_npc(Npcs.FRED_THE_FARMER, 3187, 3272, 0, 4, Direction.EAST)
spawn_item(item = Items.SHEARS, amount = 1, x = 3192, z = 3272)

on_npc_option(732, option = "talk-to") {
    player.queue {
        if (player.getVarp(179) == 0) { // NOT STARTED
            chatNpc("What are you doing on my land? You're not the one<br>who keeps leaving all my gates open and letting out all<br>my sheep are you?")
            askForQuest(this)
        }
        if (player.getVarp(179) == 1) { // STARTED
            checkItems(this)
        }
        if ((player.getVarp(179) == 2) and (!player.isInterfaceVisible(277))) { // COMPLETED
            afterQuestDialogue(this)
        }
    }
}

suspend fun askForQuest(it: QueueTask) {
    when (it.options("I'm looking for a quest.", "I'm looking for something to kill.", "I'm lost.")) {
        1 -> {
            lookingForQuest(it)
        }
        2 -> {
            it.chatPlayer("I'm looking for something to kill.")
            it.chatNpc("What, on my land? Leave my livestock alone you<br>scoundrel!")
        }
        3 -> {
            it.chatPlayer("I'm lost.")
            it.chatNpc("How can you be lost? Just follow the road east and<br>south. You'll end up in Lumbridge fairly quickly.")
        }
    }
}

suspend fun lookingForQuest(it: QueueTask) {
    it.chatPlayer("I'm looking for a quest.")
    it.chatNpc("You're after a quest, you say? Actually I could do with<br>a bit of help.")
    it.chatNpc("My sheep are getting mighty woolly. I'd be much<br>obliged if you could shear them. And while you're at it<br>spin the wool for me too.")
    it.chatNpc("Yes, that's it. Bring me 20 balls of wool. And I'm sure<br>I could sort out some sort of payment. Of course,<br>there's the small matter of The Thing.")
    when (it.options("Yes okay. I can do that.", "That doesn't sound a very exciting quest.", "What do you mean, The Thing?")) {
        1 -> CanDoThat(it)
        2 -> {
            it.chatPlayer("That doesn't sound a very exciting quest.")
            it.chatNpc("Well what do you expect if you ask a farmer for a<br>quest? Now are you going to help me or not?")
            when (it.options("Yes okay. I can do that.", "No I'll give it a miss.")) {
                1 -> CanDoThat(it)
                2 -> it.chatPlayer("No I'll give it a miss.")
            }
        }
        3 -> {
            it.chatPlayer("What do you mean, The Thing?")
            it.chatNpc("Well now, no one has ever seen The Thing. That's<br>why we call it The Thing, 'cos we don't know what it is.")
            it.chatNpc("Some say it's a black hearted shapeshifter, hungering for<br>the souls of hard working decent folk like me. Others<br>say it's just a sheep.")
            it.chatNpc("Well I don't have all day to stand around and gossip. <br>Are you going to shear my sheep or what!")
            when (it.options("Yes okay. I can do that.", "Erm I'm a bit worried about this Thing.")) {
                1 -> CanDoThat(it)
                2 -> {
                    it.chatPlayer("Erm I'm a bit worried about this Thing.")
                    it.chatNpc("I'm sure it's nothing to worry about. Just because my<br>last shearer was seen bolting out of the field screaming<br>for his life doesn't mean anything.")
                    it.chatPlayer("I'm not convinced.")
                }
            }
        }
    }
}

suspend fun CanDoThat(it: QueueTask) {
    it.chatPlayer("Yes okay. I can do that.")
    it.chatNpc("Good! Now one more thing, do you actually know how<br>to shear a sheep?")
    it.player.setVarp(179, 1)
    when (it.options("Of course!","Err. No, I don't know actually.")) {
        1 -> {
            it.chatPlayer("Of Course!")
            it.chatNpc("And you know how to spin wool into balls?")
            when (it.options("I'm something of an expert actually!","I don't know how to spin wool, sorry.")) {
                1 -> {
                    it.chatPlayer("I'm something of an expert actually!")
                    it.chatNpc("Well you can stop grinning and get to work then.")
                    it.chatNpc("I'm not paying you by the hour!")
                }
                2 -> {
                    it.chatPlayer("I don't know how to spin wool, sorry.")
                    it.chatNpc("Don't worry, it's quite simple!")
                    it.chatNpc("The nearest Spinning Wheel can be found on the first<br>floor of Lumbridge Castle.")
                    it.chatNpc("To get to Lumbridge Castle just follow the road east.")
                    it.itemMessageBox("This icon denotes a Spinning Wheel on the world map.", item = 7670, amountOrZoom = 400)
                    it.chatPlayer("Thank you!")
                }
            }
        }
        2 -> {
            it.chatPlayer("Err. No, I don't know actually.")
            it.chatNpc("Well, first things first, you need a pair of shears, there's<br>a pair in the house on the table.")
            it.chatNpc("Or you could buy your own pair from the General<br>Store in Lumbridge.")
            it.chatNpc("To get to Lumbridge travel east on the road outside.")
            it.itemMessageBox("General Stores are marked on the map by this symbol.", item = 5094, amountOrZoom = 400)
            it.chatNpc("Once you get some shears use them on the sheep in<br>my field.")
            it.chatPlayer("Sounds easy!")
            it.chatNpc("That's what they all say!")
            it.chatNpc("Some of the sheep don't like it and will run away from<br>you.  Persistence is the key.")
            it.chatNpc("Once you've collected some wool you can spin it into<br>balls.")
            it.chatNpc("Do you know how to spin wool?")
            when (it.options("I don't know how to spin wool, sorry.","I'm something of an expert actually!")) {
                1 -> {
                    it.chatPlayer("I don't know how to spin wool, sorry.")
                    it.chatNpc("Don't worry, it's quite simple!")
                    it.chatNpc("The nearest Spinning Wheel can be found on the first<br>floor of Lumbridge Castle.")
                    it.chatNpc("To get to Lumbridge Castle just follow the road east.")
                    it.itemMessageBox("This icon denotes a Spinning Wheel on the world map.", item = 7670, amountOrZoom = 400)
                    it.chatPlayer("Thank you!")

                }
                2 -> {
                    it.chatPlayer("I'm something of an expert actually!")
                    it.chatNpc("Well you can stop grinning and get to work then.")
                    it.chatNpc("I'm not paying you by the hour!")
                }
            }
        }
    }
}

suspend fun checkItems(it: QueueTask) {
    it.chatNpc("How are you doing getting those balls of wool?")
    if (it.player.inventory.contains(Items.BALL_OF_WOOL) and (ballsNeeded >= 0)) {
        it.chatPlayer("I have some.")
        it.chatNpc("Give 'em here then.")
        val currentlyHaz = it.player.inventory.getItemCount(Items.BALL_OF_WOOL)
        ballsNeeded-=currentlyHaz
        it.player.inventory.remove(item = Items.BALL_OF_WOOL, amount = currentlyHaz)
    }
    if (ballsNeeded > 0) {
        it.chatPlayer("How many more do I need to give you?")
        it.chatNpc("You need to collect $ballsNeeded more balls of wool.")
        it.chatPlayer("I haven't got any at the moment.")
        it.chatNpc("Ah well at least you haven't been eaten.")
    }
    if (ballsNeeded <= 0) {
        it.chatPlayer("That's the last of them.")
        it.chatNpc("I guess I'd better pay you then.")
        questCompleted(it)
    }
}

suspend fun questCompleted(it: QueueTask) {
    it.player.setVarp(179, 21)
    it.player.setVarp(101, it.player.getVarp(101)+1)
    it.player.message("Congratulations! Quest complete!")
    it.player.openInterface(277, dest = InterfaceDestination.MAIN_SCREEN)
    it.player.setComponentText(277, 1, "Congratulations!")
    it.player.setComponentText(277, 2, "You have completed the Sheep Shearer Quest!")
    it.player.setComponentItem(277, 3, 1891, 185)
    it.player.setComponentText(277, 4, "Quest Points: ${it.player.getVarp(101)}")
    it.player.setComponentText(277, 5, "")
    it.player.setComponentText(277, 7, "You are awarded:")
    it.player.setComponentText(277, 8, "1 Quest Point")
    it.player.setComponentText(277, 9, "150 Crafting XP")
    it.player.setComponentText(277, 10, "60 coins")
    it.player.setComponentText(277, 11, "")
    it.player.setComponentText(277, 12, "")
    it.player.setComponentText(277, 13, "")
    it.player.setComponentText(277, 14, "")
    it.player.addXp(Skills.CRAFTING, 150.0)
    it.player.inventory.add(Items.COINS_995, 60)
}

suspend fun afterQuestDialogue(it: QueueTask) {
    it.chatNpc("What are you doing on my land?")
    when (it.options("I'm looking for something to kill.", "I'm lost.")) {
        1 -> {
            it.chatPlayer("I'm looking for something to kill.")
            it.chatNpc("What, on my land? Leave my livestock alone you<br>scoundrel!")
        }
        2 -> {
            it.chatPlayer("I'm lost.")
            it.chatNpc("How can you be lost? Just follow the road east and<br>south. You'll end up in Lumbridge fairly quickly.")
        }
    }
}

//FOR TESTING
on_command("q17") {
    player.setVarp(179, 0)
    player.setVarp(101, player.getVarp(101)-1)
    player.inventory.add(Items.BALL_OF_WOOL, 20)
}