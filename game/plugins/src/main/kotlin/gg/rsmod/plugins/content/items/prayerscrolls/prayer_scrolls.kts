package gg.rsmod.plugins.content.items.prayerscrolls

import gg.rsmod.plugins.content.mechanics.prayer.Prayers

on_item_option(Items.DEXTEROUS_PRAYER_SCROLL, "read") {
    player.queue {
        if (player.getVarbit(Prayers.RIGOUR_UNLOCK_VARBIT) == 1) {
            messageBox("You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods. However there's nothing more for you to learn.")
            return@queue
        }
        player.animate(id = 7403)
        itemMessageBox("You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power? <br>(Warning: This will consume the scroll.)</b>", item = Items.DEXTEROUS_PRAYER_SCROLL)
        when (options("Learn Rigour", "Cancel", title = "This will consume the scroll")) {
            1 -> {
                if (player.inventory.contains(Items.DEXTEROUS_PRAYER_SCROLL)) {
                    player.inventory.remove(item = Items.DEXTEROUS_PRAYER_SCROLL)
                    player.setVarbit(id = Prayers.RIGOUR_UNLOCK_VARBIT, value = 1)
                    player.animate(id = -1)
                    itemMessageBox("You study the scroll and learn a new prayer: <col=8B0000>Rigour</col>", item = Items.DEXTEROUS_PRAYER_SCROLL)
                }
            }
            2 -> {
                player.animate(id = -1)
            }
        }
    }
}

on_item_option(Items.ARCANE_PRAYER_SCROLL, "read") {
    player.queue {
        if (player.getVarbit(Prayers.AUGURY_UNLOCK_VARBIT) == 1) {
            messageBox("You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods. However there's nothing more for you to learn.")
            return@queue
        }
        player.animate(id = 7403)
        itemMessageBox("You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power? <br>(Warning: This will consume the scroll.)</b>", item = Items.ARCANE_PRAYER_SCROLL)
        when (options("Learn Augury", "Cancel", title = "This will consume the scroll")) {
            1 -> {
                if (player.inventory.contains(Items.ARCANE_PRAYER_SCROLL)) {
                    player.inventory.remove(item = Items.ARCANE_PRAYER_SCROLL)
                    player.setVarbit(id = Prayers.AUGURY_UNLOCK_VARBIT, value = 1)
                    player.animate(id = -1)
                    itemMessageBox("You study the scroll and learn a new prayer: <col=8B0000>Augury</col>", item = Items.ARCANE_PRAYER_SCROLL)
                }
            }
            2 -> {
                player.animate(id = -1)
            }
        }
    }
}

on_item_option(Items.TORN_PRAYER_SCROLL, "read") {
    player.queue {
        if (player.getVarbit(Prayers.PRESERVE_UNLOCK_VARBIT) == 1) {
            messageBox("You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods. However there's nothing more for you to learn.")
            return@queue
        }
        player.animate(id = 7403)
        itemMessageBox("You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power? <br>(Warning: This will consume the scroll.)</b>", item = Items.TORN_PRAYER_SCROLL)
        when (options("Learn Preserve", "Cancel", title = "This will consume the scroll")) {
            1 -> {
                if (player.inventory.contains(Items.TORN_PRAYER_SCROLL)) {
                    player.inventory.remove(item = Items.TORN_PRAYER_SCROLL)
                    player.setVarbit(id = Prayers.PRESERVE_UNLOCK_VARBIT, value = 1)
                    player.animate(id = -1)
                    itemMessageBox("You study the scroll and learn a new prayer: <col=8B0000>Preserve</col>", item = Items.TORN_PRAYER_SCROLL)
                }
            }
            2 -> {
                player.animate(id = -1)
            }
        }
    }
}