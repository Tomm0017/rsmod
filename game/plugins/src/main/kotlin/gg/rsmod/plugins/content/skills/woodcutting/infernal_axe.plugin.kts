package gg.rsmod.plugins.content.skills.woodcutting

val infernalaxe = Item(Items.INFERNAL_AXE)

on_item_on_item(item1 = Items.DRAGON_AXE, item2 = Items.SMOULDERING_STONE) {
    Woodcutting.createAxe(player)
}
on_item_on_item(item1 = Items.INFERNAL_AXE_UNCHARGED, item2 = Items.SMOULDERING_STONE) {
    player.inventory.remove(Items.INFERNAL_AXE_UNCHARGED)
    player.inventory.remove(Items.SMOULDERING_STONE)
    player.inventory.add(Items.INFERNAL_AXE)
    Woodcutting.charges = 10
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
