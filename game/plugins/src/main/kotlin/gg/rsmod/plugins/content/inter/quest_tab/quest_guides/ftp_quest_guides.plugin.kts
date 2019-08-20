package gg.rsmod.plugins.content.inter.quest_tab.quest_guides

/**
 * On making a quest guide need to make sure you set
 * player.setComponentText(interfaceId = 119, component = 7, text = "")
 * if the quest doesnt already have a componet 7 till a better fix
 * due to sheepshearer having on open well leak through other guides if not set
 **/

on_button(interfaceId = 399, component = 6) {
    val slot = player.getInteractingSlot()
    player.closeInterface(119)
    player.runClientScript(2523, 0, 3)
    player.message("$slot")

//Cooks assistant
    if (slot == 1) {
        player.openInterface(119, dest = InterfaceDestination.MAIN_SCREEN)
        if (player.getVarp(29) == 0) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>Cook's Assistant</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<col=000080>I can start this quest by speaking to the <col=800000>Cook<col=000080> in the")
            player.setComponentText(interfaceId = 119, component = 5, text = "<col=000080><col=800000>Kitchen<col=000080> on the ground floor of <col=800000>Lumbridge Castle<col=000080>.")
            player.setComponentText(interfaceId = 119, component = 7, text = "")
        }
        if (player.getVarp(29) == 1) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>Cook's Assistant</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<col=000080>It's the <col=800000>Duke of Lumbridge's<col=000080> birthday and I have to help")
            player.setComponentText(interfaceId = 119, component = 5, text = "<col=000080>his <col=800000>Cook<col=000080> make him a <col=800000>birthday cake<col=000080>. To do this I need to")
            player.setComponentText(interfaceId = 119, component = 6, text = "<col=000080>bring him the following ingredients:")
            player.setComponentText(interfaceId = 119, component = 7, text = "<col=000080>I need to find a <col=800000>bucket of milk<col=000080>. There's a cattle field east")
            player.setComponentText(interfaceId = 119, component = 8, text = "<col=000080>of Lumbridge, I should make sure I take an empty bucket")
            player.setComponentText(interfaceId = 119, component = 9, text = "<col=000080>with me.")
            player.setComponentText(interfaceId = 119, component = 10, text = "<col=000080>I need to find a <col=800000>pot of flour<col=000080>. There's a mill found north-")
            player.setComponentText(interfaceId = 119, component = 11, text = "<col=000080>west of Lumbridge, I should take an empty pot with me.")
            player.setComponentText(interfaceId = 119, component = 12, text = "<col=000080>I need to find an <col=800000>egg<col=000080>. The cook normally gets his eggs from")
            player.setComponentText(interfaceId = 119, component = 13, text = "<col=000080>the Groats' farm, found just to the west of the cattle")
            player.setComponentText(interfaceId = 119, component = 14, text = "<col=000080>field.")
            player.runClientScript(2523, 0, 11)
        }
        if (player.getVarp(29) == 2) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>Cook's Assistant</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<str>It was the Duke of Lumbridge's birthday, but his cook had")
            player.setComponentText(interfaceId = 119, component = 5, text = "<str>forgotten to buy the ingredients he needed to make him a")
            player.setComponentText(interfaceId = 119, component = 6, text = "<str>cake. I brought the cook an egg, some flour and some milk")
            player.setComponentText(interfaceId = 119, component = 7, text = "<str>and the cook made a delicious looking cake with them.")
            player.setComponentText(interfaceId = 119, component = 8, text = "")
            player.setComponentText(interfaceId = 119, component = 9, text = "<str>As a reward he now lets me use his high quality range")
            player.setComponentText(interfaceId = 119, component = 10, text = "<str>which lets me burn things less whenever I wish to cook")
            player.setComponentText(interfaceId = 119, component = 11, text = "<str>there.")
            player.setComponentText(interfaceId = 119, component = 12, text = "")
            player.setComponentText(interfaceId = 119, component = 13, text = "<col=ff0000>QUEST COMPLETE!")
            player.runClientScript(2523, 0, 10)
        }
    }

//Sheep Shearer
    if (slot == 16) {
        player.openInterface(119, dest = InterfaceDestination.MAIN_SCREEN)
        if (player.getVarp(179) == 0) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>Sheep Shearer</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<col=000080>I can start this quest by speaking to <col=800000>Farmer Fred<col=000080> at his")
            player.setComponentText(interfaceId = 119, component = 5, text = "<col=000080><col=800000>farm<col=000080> just a little way <col=800000>North West of Lumbridge")
            player.setComponentText(interfaceId = 119, component = 7, text = "")
        }
        if (player.getVarp(179) == 1) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>Sheep Shearer</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<str>I asked Farmer Fred, near Lumbridge, for a quest. Fred")
            player.setComponentText(interfaceId = 119, component = 5, text = "<str>said he'd pay me for shearing his sheep for him!")
        }
        if (player.getVarp(179) == 2) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>Sheep Shearer</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<str>I brought Farmer Fred 20 balls of wool, and he paid me for")
            player.setComponentText(interfaceId = 119, component = 5, text = "<str>it!")
            player.setComponentText(interfaceId = 119, component = 6, text = "")
            player.setComponentText(interfaceId = 119, component = 7, text = "<col=ff0000>QUEST COMPLETE!")
        }
    }

//X Marks the Spot
    if (slot == 20) {
        player.openInterface(119, dest = InterfaceDestination.MAIN_SCREEN)
        if (player.getVarp(2111) == 0) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>X Marks the Spot</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<col=000080>I can start this quest by talking to <col=800000>Veos<col=000080> in the <col=800000>Sheared")
            player.setComponentText(interfaceId = 119, component = 5, text = "<col=800000>Ram Pub<col=000080> in <col=800000>Lumbridge<col=000080>.")
            player.setComponentText(interfaceId = 119, component = 6, text = "")
            player.setComponentText(interfaceId = 119, component = 7, text = "<col=000080>This quest has no requirements.")
        }
        if (player.getVarp(2111) == 1) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>X Marks the Spot</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<str>I spoke to Veos in the Sheared Ram Pub in Lumbridge. He")
            player.setComponentText(interfaceId = 119, component = 5, text = "<str>told me that he was a traveller from a land to the far west")
            player.setComponentText(interfaceId = 119, component = 6, text = "<str>known as Great Kourend and that he had come to")
            player.setComponentText(interfaceId = 119, component = 7, text = "<str>Lumbridge in search of treasure. He asked me to help him")
            player.setComponentText(interfaceId = 119, component = 8, text = "<str>find this treasure.")
            player.setComponentText(interfaceId = 119, component = 9, text = "<col=800000>Veos<col=000080> gave me a <col=800000>Treasure Scroll<col=000080>. I should use it to find the")
            player.setComponentText(interfaceId = 119, component = 10, text = "<col=000080>treasure.")
        }
        if (player.getVarp(2111) == 2) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>X Marks the Spot</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<str>I spoke to Veos in the Sheared Ram Pub in Lumbridge. He")
            player.setComponentText(interfaceId = 119, component = 5, text = "<str>told me that he was a traveller from a land to the far west")
            player.setComponentText(interfaceId = 119, component = 6, text = "<str>known as Great Kourend and that he had come to")
            player.setComponentText(interfaceId = 119, component = 7, text = "<str>Lumbridge in search of treasure. He asked me to help him")
            player.setComponentText(interfaceId = 119, component = 8, text = "<str>find this treasure.")
            player.setComponentText(interfaceId = 119, component = 9, text = "<col=800000>Veos<col=000080> gave me a <col=800000>Treasure Scroll<col=000080> which I used to help me")
            player.setComponentText(interfaceId = 119, component = 10, text = "<col=000080>find a second <col=800000>Treasure Scroll<col=000080>. I should see if the second")
            player.setComponentText(interfaceId = 119, component = 11, text = "<col=000080>scroll leads me to the treasure.")
        }
        if (player.getVarp(2111) == 3) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>X Marks the Spot</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<str>I spoke to Veos in the Sheared Ram Pub in Lumbridge. He")
            player.setComponentText(interfaceId = 119, component = 5, text = "<str>told me that he was a traveller from a land to the far west")
            player.setComponentText(interfaceId = 119, component = 6, text = "<str>known as Great Kourend and that he had come to")
            player.setComponentText(interfaceId = 119, component = 7, text = "<str>Lumbridge in search of treasure. He asked me to help him")
            player.setComponentText(interfaceId = 119, component = 8, text = "<str>find this treasure.")
            player.setComponentText(interfaceId = 119, component = 9, text = "<str>Veos gave me a Treasure Scroll which I used to help me")
            player.setComponentText(interfaceId = 119, component = 10, text = "<str>find a second Treasure Scroll.")
            player.setComponentText(interfaceId = 119, component = 11, text = "<col=000080>Using the second <col=800000>Treasure Scroll<col=000080>, I managed to find a")
            player.setComponentText(interfaceId = 119, component = 12, text = "<col=000080><col=800000>Mysterious Orb<col=000080>. I should see if I can use the orb to find")
            player.setComponentText(interfaceId = 119, component = 13, text = "<col=000080>the treasure.")
            player.runClientScript(2523, 0, 12)
        }
        if (player.getVarp(2111) == 4) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>X Marks the Spot</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<str>I spoke to Veos in the Sheared Ram Pub in Lumbridge. He")
            player.setComponentText(interfaceId = 119, component = 5, text = "<str>told me that he was a traveller from a land to the far west")
            player.setComponentText(interfaceId = 119, component = 6, text = "<str>known as Great Kourend and that he had come to")
            player.setComponentText(interfaceId = 119, component = 7, text = "<str>Lumbridge in search of treasure. He asked me to help him")
            player.setComponentText(interfaceId = 119, component = 8, text = "<str>find this treasure.")
            player.setComponentText(interfaceId = 119, component = 9, text = "<str>Veos gave me a Treasure Scroll which I used to help me")
            player.setComponentText(interfaceId = 119, component = 10, text = "<str>find a second Treasure Scroll.")
            player.setComponentText(interfaceId = 119, component = 11, text = "<str>Using the second Treasure Scroll, I managed to find a")
            player.setComponentText(interfaceId = 119, component = 12, text = "<str>Mysterious Orb.")
            player.setComponentText(interfaceId = 119, component = 13, text = "<col=000080>The <col=800000>Mysterious Orb<col=000080> led me to a third <col=800000>Treasure Scroll<col=000080>.")
            player.setComponentText(interfaceId = 119, component = 14, text = "<col=000080>Maybe this scroll will lead me to the treasure.")
            player.runClientScript(2523, 0, 12)
        }
        if (player.getVarp(2111) == 5) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>X Marks the Spot</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<str>I spoke to Veos in the Sheared Ram Pub in Lumbridge. He")
            player.setComponentText(interfaceId = 119, component = 5, text = "<str>told me that he was a traveller from a land to the far west")
            player.setComponentText(interfaceId = 119, component = 6, text = "<str>known as Great Kourend and that he had come to")
            player.setComponentText(interfaceId = 119, component = 7, text = "<str>Lumbridge in search of treasure. He asked me to help him")
            player.setComponentText(interfaceId = 119, component = 8, text = "<str>find this treasure.")
            player.setComponentText(interfaceId = 119, component = 9, text = "<str>Veos gave me a Treasure Scroll which I used to help me")
            player.setComponentText(interfaceId = 119, component = 10, text = "<str>find a second Treasure Scroll.")
            player.setComponentText(interfaceId = 119, component = 11, text = "<str>Using the second Treasure Scroll, I managed to find a")
            player.setComponentText(interfaceId = 119, component = 12, text = "<str>Mysterious Orb.")
            player.setComponentText(interfaceId = 119, component = 13, text = "<str>The Mysterious Orb led me to a third Treasure Scroll.")
            player.setComponentText(interfaceId = 119, component = 14, text = "<col=000080>By using the third <col=800000>Treasure Scroll<col=000080>, I managed to find an")
            player.setComponentText(interfaceId = 119, component = 15, text = "<col=000080><col=800000>Ancient Casket<col=000080>. I should take it to <col=800000>Veos<col=000080> at his ship on the")
            player.setComponentText(interfaceId = 119, component = 16, text = "<col=000080>northernmost dock in <col=800000>Port Sarim<col=000080>.")
            player.runClientScript(2523, 0, 14)
        }
        if (player.getVarp(2111) == 8) {
            player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>X Marks the Spot</col>")
            player.setComponentText(interfaceId = 119, component = 4, text = "<str>I spoke to Veos in the Sheared Ram Pub in Lumbridge. He")
            player.setComponentText(interfaceId = 119, component = 5, text = "<str>told me that he was a traveller from a land to the far west")
            player.setComponentText(interfaceId = 119, component = 6, text = "<str>known as Great Kourend and that he had come to")
            player.setComponentText(interfaceId = 119, component = 7, text = "<str>Lumbridge in search of treasure. He asked me to help him")
            player.setComponentText(interfaceId = 119, component = 8, text = "<str>find this treasure.")
            player.setComponentText(interfaceId = 119, component = 9, text = "<str>Veos gave me a Treasure Scroll which I used to help me")
            player.setComponentText(interfaceId = 119, component = 10, text = "<str>find a second Treasure Scroll.")
            player.setComponentText(interfaceId = 119, component = 11, text = "<str>Using the second Treasure Scroll, I managed to find a")
            player.setComponentText(interfaceId = 119, component = 12, text = "<str>Mysterious Orb.")
            player.setComponentText(interfaceId = 119, component = 13, text = "<str>The Mysterious Orb led me to a third Treasure Scroll.")
            player.setComponentText(interfaceId = 119, component = 14, text = "<str>By using the third Treasure Scroll, I managed to find an")
            player.setComponentText(interfaceId = 119, component = 15, text = "<str>Ancient Casket.")
            player.setComponentText(interfaceId = 119, component = 16, text = "<str>I took the Ancient Casket to Veos at his ship in Port Sarim.")
            player.setComponentText(interfaceId = 119, component = 17, text = "<str>He thanked me for my help and rewarded me.")
            player.setComponentText(interfaceId = 119, component = 18, text = "")
            player.setComponentText(interfaceId = 119, component = 19, text = "<col=ff0000>QUEST COMPLETE!")
            player.runClientScript(2523, 0, 16)
        }
    }
}
