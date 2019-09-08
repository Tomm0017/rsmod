package gg.rsmod.plugins.content.skills.woodcutting

import gg.rsmod.plugins.api.cfg.Items

on_item_on_item(item1 = Items.DRAGON_AXE, item2 = Items.SMOULDERING_STONE) {
    Woodcutting.createAxe(player)
}

val rechargeItem = intArrayOf(Items.SMOULDERING_STONE, Items.DRAGON_AXE)

rechargeItem.forEach { item ->
    on_item_on_item(item1 = Items.INFERNAL_AXE_UNCHARGED, item2 = item) {
        player.inventory.remove(Items.INFERNAL_AXE_UNCHARGED)
        player.inventory.remove(item)
        player.inventory.add(Items.INFERNAL_AXE)
        Woodcutting.charges = 5000
    }
}

on_item_option(Items.INFERNAL_AXE, "check") {
    Woodcutting.checkCharges(player)
}
on_equipment_option(Items.INFERNAL_AXE, "check") {
    Woodcutting.checkCharges(player)
}

on_logout {
    Woodcutting.charges = Woodcutting.charges
}