package gg.rsmod.plugins.content.areas.alkharid.chat

val INTERFACE_ID = 324

on_interface_open(INTERFACE_ID) {
    //Soft leather
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 100, item = 1739, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 108, text = "Soft leather")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 116, text = "1 coins")
    //Hard leather
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 101, item = 1739, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 109, text = "Hard leather")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 117, text = "3 coins")
    //Snake skin
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 102, item = 6287, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 110, text = "Snakeskin")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 118, text = "20 coins")
    //Snake hide
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 103, item = 7801, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 111, text = "Snakeskin")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 119, text = "15 coins")
    //Green d'hide
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 104, item = 1753, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 112, text = "Green d'hide")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 120, text = "20 coins")
    //Blue d'hide
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 105, item = 1751, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 113, text = "Blue d'hide")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 121, text = "20 coins")
    //Red d'hide
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 106, item = 1749, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 114, text = "Red d'hide")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 122, text = "20 coins")
    //Black d'hide
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 107, item = 1747, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 115, text = "Black d'hide")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 123, text = "20 coins")

}
//Soft Leather
on_button(interfaceId = 324, component = 148) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) <  1) {
        player.message("You haven't got enough coins to pay for soft leather.")
    } else if (inventory.getItemCount(Items.COWHIDE) < 1) {
        player.message("You don't have any cowhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >=  1 && inventory.getItemCount(Items.COWHIDE) >=  1) {
        inventory.remove(Items.COWHIDE, 1)
        inventory.remove(Items.COINS_995, 1)
        inventory.add(Items.LEATHER)
    }
}
on_button(interfaceId = 324, component = 140) {
    //Make-5
}
on_button(interfaceId = 324, component = 132) {
    var amount: Int
    val inventory = player.inventory

    player.queue(TaskPriority.WEAK) {
        amount = inputInt("Enter amount:")
        if (inventory.getItemCount(Items.COINS_995) < 1) {
            player.message("You haven't got enough coins to pay for soft leather.")
        } else if (inventory.getItemCount(Items.COWHIDE) < 1) {
            player.message("You don't have any cowhides to tan.")
        }
        if (inventory.getItemCount(Items.COINS_995) >= 1 && inventory.getItemCount(Items.COWHIDE) >= 1 && amount > 0) {
            inventory.remove(Items.COWHIDE, amount)
            inventory.remove(Items.COINS_995, amount)
            inventory.add(Items.LEATHER, amount)
        }
    }
}
on_button(interfaceId = 324, component = 124) {
    val inventory = player.inventory

    if (inventory.getItemCount(Items.COINS_995) < 1) {
        player.message("You haven't got enough coins to pay for soft leather.")
    } else if (inventory.getItemCount(Items.COWHIDE) < 1) {
        player.message("You don't have any cowhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >= 1 && inventory.getItemCount(Items.COWHIDE) >= 1) {

        val tanAll = inventory.remove(item = Items.COWHIDE, amount = inventory.getItemCount(Items.COWHIDE))
        val count = (tanAll.items.size)

        if (tanAll.hasSucceeded()) {
            player.inventory.remove(Items.COINS_995, count)
            player.inventory.add(Items.LEATHER, count)
        }
    }
}
//Hard Leather
on_button(interfaceId = 324, component = 149) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) <  3) {
        player.message("You haven't got enough coins to pay for hard leather.")
    } else if (inventory.getItemCount(Items.COWHIDE) < 1) {
        player.message("You don't have any cowhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >=  3 && inventory.getItemCount(Items.COWHIDE) >=  1) {
        inventory.remove(Items.COWHIDE, 1)
        inventory.remove(Items.COINS_995, 3)
        inventory.add(Items.HARD_LEATHER)
    }
}
on_button(interfaceId = 324, component = 141) {
    //Make-5
}
on_button(interfaceId = 324, component = 133) {
    var amount: Int
    val inventory = player.inventory

    player.queue(TaskPriority.WEAK) {
        amount = inputInt("Enter amount:")
        if (inventory.getItemCount(Items.COINS_995) < 3) {
            player.message("You haven't got enough coins to pay for hard leather.")
        } else if (inventory.getItemCount(Items.COWHIDE) < 1) {
            player.message("You don't have any cowhides to tan.")
        }
        if (inventory.getItemCount(Items.COINS_995) >= 3 && inventory.getItemCount(Items.COWHIDE) >= 1 && amount > 0) {
            inventory.remove(Items.COWHIDE, amount)
            inventory.remove(Items.COINS_995, amount*3)
            inventory.add(Items.HARD_LEATHER, amount)
        }
    }
}
on_button(interfaceId = 324, component = 125) {
    val inventory = player.inventory

    if (inventory.getItemCount(Items.COINS_995) < 3) {
        player.message("You haven't got enough coins to pay for hard leather.")
    } else if (inventory.getItemCount(Items.COWHIDE) < 1) {
        player.message("You don't have any cowhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >= 3 && inventory.getItemCount(Items.COWHIDE) >= 1) {

        val tanAll = inventory.remove(item = Items.COWHIDE, amount = inventory.getItemCount(Items.COWHIDE))
        val count = (tanAll.items.size)

        if (tanAll.hasSucceeded()) {
            player.inventory.remove(Items.COINS_995, count*3)
            player.inventory.add(Items.HARD_LEATHER, count)
        }
    }
}
//Snake-Hide Norm
on_button(interfaceId = 324, component = 150) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) <  20) {
        player.message("You haven't got enough coins to pay for snake skin.")
    } else if (inventory.getItemCount(Items.SNAKE_HIDE) < 1) {
        player.message("You don't have any snake hides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >=  20 && inventory.getItemCount(Items.SNAKE_HIDE) >=  1) {
        inventory.remove(Items.SNAKE_HIDE, 1)
        inventory.remove(Items.COINS_995, 20)
        inventory.add(Items.SNAKESKIN)
    }
}
on_button(interfaceId = 324, component = 142) {
    //Make-5
}
on_button(interfaceId = 324, component = 134) {
    var amount: Int
    val inventory = player.inventory

    player.queue(TaskPriority.WEAK) {
        amount = inputInt("Enter amount:")
        if (inventory.getItemCount(Items.COINS_995) < 20) {
            player.message("You haven't got enough coins to pay for snake skin.")
        } else if (inventory.getItemCount(Items.SNAKE_HIDE) < 1) {
            player.message("You don't have any snake hides to tan.")
        }
        if (inventory.getItemCount(Items.COINS_995) >= 20 && inventory.getItemCount(Items.SNAKE_HIDE) >= 1 && amount > 0) {
            inventory.remove(Items.COWHIDE, amount)
            inventory.remove(Items.COINS_995, amount*20)
            inventory.add(Items.SNAKESKIN, amount)
        }
    }
}
on_button(interfaceId = 324, component = 126) {
    val inventory = player.inventory

    if (inventory.getItemCount(Items.COINS_995) < 20) {
        player.message("You haven't got enough coins to pay for snake skin.")
    } else if (inventory.getItemCount(Items.SNAKE_HIDE) < 1) {
        player.message("You don't have any snake hides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >= 20 && inventory.getItemCount(Items.SNAKE_HIDE) >= 1) {

        val tanAll = inventory.remove(item = Items.SNAKE_HIDE, amount = inventory.getItemCount(Items.SNAKE_HIDE))
        val count = (tanAll.items.size)

        if (tanAll.hasSucceeded()) {
            player.inventory.remove(Items.COINS_995, count*20)
            player.inventory.add(Items.SNAKESKIN, count)
        }
    }
}
//Snake-Hide Temple Trekking
on_button(interfaceId = 324, component = 151) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) <  15) {
        player.message("You haven't got enough coins to pay for snake skin.")
    } else if (inventory.getItemCount(Items.SNAKE_HIDE_7801) < 1) {
        player.message("You don't have any snake hides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >=  15 && inventory.getItemCount(Items.SNAKE_HIDE_7801) >=  1) {
        inventory.remove(Items.SNAKE_HIDE_7801, 1)
        inventory.remove(Items.COINS_995, 15)
        inventory.add(Items.SNAKESKIN)
    }
}
on_button(interfaceId = 324, component = 143) {
    //Make-5
}
on_button(interfaceId = 324, component = 135) {
    var amount: Int
    val inventory = player.inventory

    player.queue(TaskPriority.WEAK) {
        amount = inputInt("Enter amount:")
        if (inventory.getItemCount(Items.COINS_995) < 15) {
            player.message("You haven't got enough coins to pay for snake skin.")
        } else if (inventory.getItemCount(Items.SNAKE_HIDE_7801) < 1) {
            player.message("You don't have any snake hides to tan.")
        }
        if (inventory.getItemCount(Items.COINS_995) >= 15 && inventory.getItemCount(Items.SNAKE_HIDE_7801) >= 1 && amount > 0) {
            inventory.remove(Items.COWHIDE, amount)
            inventory.remove(Items.COINS_995, amount*15)
            inventory.add(Items.SNAKESKIN, amount)
        }
    }
}
on_button(interfaceId = 324, component = 127) {
    val inventory = player.inventory

    if (inventory.getItemCount(Items.COINS_995) < 15) {
        player.message("You haven't got enough coins to pay for snake skin.")
    } else if (inventory.getItemCount(Items.SNAKE_HIDE_7801) < 1) {
        player.message("You don't have any snake hides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >= 15 && inventory.getItemCount(Items.SNAKE_HIDE_7801) >= 1) {

        val tanAll = inventory.remove(item = Items.SNAKE_HIDE_7801, amount = inventory.getItemCount(Items.SNAKE_HIDE_7801))
        val count = (tanAll.items.size)

        if (tanAll.hasSucceeded()) {
            player.inventory.remove(Items.COINS_995, count*15)
            player.inventory.add(Items.SNAKESKIN, count)
        }
    }
}
//Green D'Hide
on_button(interfaceId = 324, component = 152) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) <  20) {
        player.message("You haven't got enough coins to pay for green d'hide.")
    } else if (inventory.getItemCount(Items.GREEN_DRAGONHIDE) < 1) {
        player.message("You don't have any green dragonhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >=  20 && inventory.getItemCount(Items.GREEN_DRAGONHIDE) >=  1) {
        inventory.remove(Items.GREEN_DRAGONHIDE, 1)
        inventory.remove(Items.COINS_995, 20)
        inventory.add(Items.GREEN_DRAGON_LEATHER)
    }
}
on_button(interfaceId = 324, component = 144) {
    //Make-5
}
on_button(interfaceId = 324, component = 136) {
    var amount: Int
    val inventory = player.inventory

    player.queue(TaskPriority.WEAK) {
        amount = inputInt("Enter amount:")
        if (inventory.getItemCount(Items.COINS_995) < 20) {
            player.message("You haven't got enough coins to pay for green d'hide.")
        } else if (inventory.getItemCount(Items.GREEN_DRAGONHIDE) < 1) {
            player.message("You don't have any green dragonhides to tan.")
        }
        if (inventory.getItemCount(Items.COINS_995) >= 20 && inventory.getItemCount(Items.GREEN_DRAGONHIDE) >= 1 && amount > 0) {
            inventory.remove(Items.GREEN_DRAGONHIDE, amount)
            inventory.remove(Items.COINS_995, amount*20)
            inventory.add(Items.GREEN_DRAGON_LEATHER, amount)
        }
    }
}
on_button(interfaceId = 324, component = 128) {
    val inventory = player.inventory

    if (inventory.getItemCount(Items.COINS_995) < 20) {
        player.message("You haven't got enough coins to pay for green d'hide.")
    } else if (inventory.getItemCount(Items.GREEN_DRAGONHIDE) < 1) {
        player.message("You don't have any green dragonhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >= 20 && inventory.getItemCount(Items.GREEN_DRAGONHIDE) >= 1) {

        val tanAll = inventory.remove(item = Items.GREEN_DRAGONHIDE, amount = inventory.getItemCount(Items.GREEN_DRAGONHIDE))
        val count = (tanAll.items.size)

        if (tanAll.hasSucceeded()) {
            player.inventory.remove(Items.COINS_995, count*20)
            player.inventory.add(Items.GREEN_DRAGON_LEATHER, count)
        }
    }
}
//Blue D'Hide
on_button(interfaceId = 324, component = 153) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) <  20) {
        player.message("You haven't got enough coins to pay for blue d'hide.")
    } else if (inventory.getItemCount(Items.BLUE_DRAGONHIDE) < 1) {
        player.message("You don't have any blue dragonhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >=  20 && inventory.getItemCount(Items.BLUE_DRAGONHIDE) >=  1) {
        inventory.remove(Items.BLUE_DRAGONHIDE, 1)
        inventory.remove(Items.COINS_995, 20)
        inventory.add(Items.BLUE_DRAGON_LEATHER)
    }
}
on_button(interfaceId = 324, component = 145) {
    //Make-5
}
on_button(interfaceId = 324, component = 137) {
    var amount: Int
    val inventory = player.inventory

    player.queue(TaskPriority.WEAK) {
        amount = inputInt("Enter amount:")
        if (inventory.getItemCount(Items.COINS_995) < 20) {
            player.message("You haven't got enough coins to pay for blue d'hide.")
        } else if (inventory.getItemCount(Items.BLUE_DRAGONHIDE) < 1) {
            player.message("You don't have any blue dragonhides to tan.")
        }
        if (inventory.getItemCount(Items.COINS_995) >= 20 && inventory.getItemCount(Items.BLUE_DRAGONHIDE) >= 1 && amount > 0) {
            inventory.remove(Items.BLUE_DRAGONHIDE, amount)
            inventory.remove(Items.COINS_995, amount*20)
            inventory.add(Items.BLUE_DRAGON_LEATHER, amount)
        }
    }
}
on_button(interfaceId = 324, component = 129) {
    val inventory = player.inventory

    if (inventory.getItemCount(Items.COINS_995) < 20) {
        player.message("You haven't got enough coins to pay for blue d'hide.")
    } else if (inventory.getItemCount(Items.BLUE_DRAGONHIDE) < 1) {
        player.message("You don't have any blue dragonhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >= 20 && inventory.getItemCount(Items.BLUE_DRAGONHIDE) >= 1) {

        val tanAll = inventory.remove(item = Items.BLUE_DRAGONHIDE, amount = inventory.getItemCount(Items.BLUE_DRAGONHIDE))
        val count = (tanAll.items.size)

        if (tanAll.hasSucceeded()) {
            player.inventory.remove(Items.COINS_995, count*20)
            player.inventory.add(Items.BLUE_DRAGON_LEATHER, count)
        }
    }
}
//Red D'Hide
on_button(interfaceId = 324, component = 154) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) <  20) {
        player.message("You haven't got enough coins to pay for red d'hide.")
    } else if (inventory.getItemCount(Items.RED_DRAGONHIDE) < 1) {
        player.message("You don't have any red dragonhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >=  20 && inventory.getItemCount(Items.RED_DRAGONHIDE) >=  1) {
        inventory.remove(Items.RED_DRAGONHIDE, 1)
        inventory.remove(Items.COINS_995, 20)
        inventory.add(Items.RED_DRAGON_LEATHER)
    }
}
on_button(interfaceId = 324, component = 146) {
    //Make-5
}
on_button(interfaceId = 324, component = 138) {
    var amount: Int
    val inventory = player.inventory

    player.queue(TaskPriority.WEAK) {
        amount = inputInt("Enter amount:")
        if (inventory.getItemCount(Items.COINS_995) < 20) {
            player.message("You haven't got enough coins to pay for red d'hide.")
        } else if (inventory.getItemCount(Items.RED_DRAGONHIDE) < 1) {
            player.message("You don't have any red dragonhides to tan.")
        }
        if (inventory.getItemCount(Items.COINS_995) >= 20 && inventory.getItemCount(Items.RED_DRAGONHIDE) >= 1 && amount > 0) {
            inventory.remove(Items.RED_DRAGONHIDE, amount)
            inventory.remove(Items.COINS_995, amount*20)
            inventory.add(Items.RED_DRAGON_LEATHER, amount)
        }
    }
}
on_button(interfaceId = 324, component = 130) {
    val inventory = player.inventory

    if (inventory.getItemCount(Items.COINS_995) < 20) {
        player.message("You haven't got enough coins to pay for red d'hide.")
    } else if (inventory.getItemCount(Items.RED_DRAGONHIDE) < 1) {
        player.message("You don't have any red dragonhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >= 20 && inventory.getItemCount(Items.RED_DRAGONHIDE) >= 1) {

        val tanAll = inventory.remove(item = Items.RED_DRAGONHIDE, amount = inventory.getItemCount(Items.RED_DRAGONHIDE))
        val count = (tanAll.items.size)

        if (tanAll.hasSucceeded()) {
            player.inventory.remove(Items.COINS_995, count*20)
            player.inventory.add(Items.RED_DRAGON_LEATHER, count)
        }
    }
}
//Black D'Hide
on_button(interfaceId = 324, component = 155) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) <  20) {
        player.message("You haven't got enough coins to pay for black d'hide.")
    } else if (inventory.getItemCount(Items.BLACK_DRAGONHIDE) < 1) {
        player.message("You don't have any black dragonhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >=  20 && inventory.getItemCount(Items.BLACK_DRAGONHIDE) >=  1) {
        inventory.remove(Items.BLACK_DRAGONHIDE, 1)
        inventory.remove(Items.COINS_995, 20)
        inventory.add(Items.BLACK_DRAGON_LEATHER)
    }
}
on_button(interfaceId = 324, component = 147) {
    //Make-5
}
on_button(interfaceId = 324, component = 139) {
    var amount: Int
    val inventory = player.inventory

    player.queue(TaskPriority.WEAK) {
        amount = inputInt("Enter amount:")
        if (inventory.getItemCount(Items.COINS_995) < 20) {
            player.message("You haven't got enough coins to pay for black d'hide.")
        } else if (inventory.getItemCount(Items.BLACK_DRAGONHIDE) < 1) {
            player.message("You don't have any black dragonhides to tan.")
        }
        if (inventory.getItemCount(Items.COINS_995) >= 20 && inventory.getItemCount(Items.BLACK_DRAGONHIDE) >= 1 && amount > 0) {
            inventory.remove(Items.BLACK_DRAGONHIDE, amount)
            inventory.remove(Items.COINS_995, amount*20)
            inventory.add(Items.BLACK_DRAGON_LEATHER, amount)
        }
    }
}
on_button(interfaceId = 324, component = 131) {
    val inventory = player.inventory

    if (inventory.getItemCount(Items.COINS_995) < 20) {
        player.message("You haven't got enough coins to pay for black d'hide.")
    } else if (inventory.getItemCount(Items.BLACK_DRAGONHIDE) < 1) {
        player.message("You don't have any black dragonhides to tan.")
    }
    if (inventory.getItemCount(Items.COINS_995) >= 20 && inventory.getItemCount(Items.BLACK_DRAGONHIDE) >= 1) {

        val tanAll = inventory.remove(item = Items.BLACK_DRAGONHIDE, amount = inventory.getItemCount(Items.BLACK_DRAGONHIDE))
        val count = (tanAll.items.size)

        if (tanAll.hasSucceeded()) {
            player.inventory.remove(Items.COINS_995, count*20)
            player.inventory.add(Items.BLACK_DRAGON_LEATHER, count)
        }
    }
}