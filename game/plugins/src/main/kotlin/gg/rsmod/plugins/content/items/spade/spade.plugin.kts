package gg.rsmod.plugins.content.items.spade


on_item_option(item = Items.SPADE, option = "dig") {
    player.animate(830)
    if (player.tile.x == 3229 && player.tile.z == 3209 && player.inventory.contains(Items.TREASURE_SCROLL)) {
        player.queue {
            player.inventory.remove(23067, 1)
            player.inventory.add(23068, 1)
            player.setVarp(2111, 2)
            itemMessageBox("You dig up a Treasure Scroll.", item = 23068)
        }
    }
    else if (player.tile.x == 3202 && player.tile.z == 3211 && player.inventory.contains(Items.TREASURE_SCROLL_23068)) {
        player.queue {
            player.inventory.remove(23068, 1)
            player.inventory.add(23069, 1)
            player.setVarp(2111, 3)
            itemMessageBox("You dig up a Mysterious Orb.", item = 23069)
        }
    }
    else if (player.tile.x == 3108 && player.tile.z == 3264 && player.inventory.contains(Items.MYSTERIOUS_ORB_23069)) {
        player.queue {
            player.inventory.remove(23069, 1)
            player.inventory.add(23070, 1)
            player.setVarp(2111, 4)
            itemMessageBox("You dig up a Treasure Scroll.", item = 23070)
        }
    }
    else if (player.tile.x == 3077 && player.tile.z == 3260 && player.inventory.contains(Items.TREASURE_SCROLL_23070)) {
        player.queue {
            player.inventory.remove(23070, 1)
            player.inventory.add(23071, 1)
            player.setVarp(2111, 5)
            itemMessageBox("You dig up an Ancient Casket. As you do, you hear a<br>faint whispering. You can't make out what it says<br>though...", item = 23071)
            chatPlayer("Hmmmm... Must have been the wind.")
            chatPlayer("Anyway, this must be the treasure that Veos is after. I<br>should take it to him. If I remember right, he's docked<br>at the northernmost pier in Port Sarim.")
        }
    }
    else player.message("Nothing interesting happens.")
}