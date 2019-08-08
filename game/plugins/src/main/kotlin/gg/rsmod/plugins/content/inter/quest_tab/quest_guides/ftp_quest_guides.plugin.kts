package gg.rsmod.plugins.content.inter.quest_tab.quest_guides

    on_button(interfaceId = 399, component = 6) {
        val slot = player.getInteractingSlot()

        player.openInterface(119, dest = InterfaceDestination.MAIN_SCREEN)
        player.runClientScript(2523, 0, 5)
        player.message("$slot")

//Sheep Shearer
        if (slot == 16) {
            if (player.getVarp(179) == 0) {
                player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>Sheep Shearer</col>")
                player.setComponentText(interfaceId = 119, component = 4, text = "<col=000080>I can start this quest by speaking to <col=800000>Farmer Fred<col=000080> at his")
                player.setComponentText(interfaceId = 119, component = 5, text = "<col=000080><col=800000>farm<col=000080> just a little way <col=800000>North West of Lumbridge")
            }
            if (player.getVarp(179) == 1) {
                player.setComponentText(interfaceId = 119, component = 2, text = "<col=7f0000>Sheep Shearer</col>")
                player.setComponentText(interfaceId = 119, component = 4, text = "<str>I asked Farmer Fred, near Lumbridge, for a quest. Fred")
                player.setComponentText(interfaceId = 119, component = 5, text = "<str>said he'd pay me for shearing his sheep for him!")
                player.setComponentText(interfaceId = 119, component = 6, text = "")
                player.setComponentText(interfaceId = 119, component = 7, text = "<col=000080>I need to collect 20 more <col=800000>balls of wool.")
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
