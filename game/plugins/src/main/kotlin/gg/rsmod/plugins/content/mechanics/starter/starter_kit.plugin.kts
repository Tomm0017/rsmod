package gg.rsmod.plugins.content.mechanics.starter

import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR

load_metadata {
    propertyFileName = "starter_kit"

    author = "Tomm"
    name = "Starter Kit"
    description = "Give items to new accounts."

    properties(
            // Inventory first row
            0.getPropertyItem() to Items.IRON_FULL_HELM,
            1.getPropertyItem() to Items.IRON_PLATEBODY,
            2.getPropertyItem() to Items.IRON_PLATELEGS,
            3.getPropertyItem() to Items.IRON_SCIMITAR,

            // Inventory second row
            4.getPropertyItem() to Items.BLUE_WIZARD_HAT,
            5.getPropertyItem() to Items.BLUE_WIZARD_ROBE,
            6.getPropertyItem() to Items.BLUE_SKIRT,
            7.getPropertyItem() to Items.STAFF_OF_AIR,

            // Inventory third row
            8.getPropertyItem() to Items.LEATHER_COWL,
            9.getPropertyItem() to Items.LEATHER_BODY,
            10.getPropertyItem() to Items.LEATHER_CHAPS,
            11.getPropertyItem() to Items.SHORTBOW,

            // Inventory fourth row
            12.getPropertyItem() to Items.AMULET_OF_POWER,
            13.getPropertyItem() to Items.LEATHER_GLOVES,
            14.getPropertyItem() to Items.CLIMBING_BOOTS,
            15.getPropertyItem() to Items.IRON_ARROW,
            15.getPropertyAmount() to 1000,

            // Inventory fifth row
            16.getPropertyItem() to Items.AIR_RUNE,
            16.getPropertyAmount() to 1000,
            17.getPropertyItem() to Items.WATER_RUNE,
            17.getPropertyAmount() to 1000,
            18.getPropertyItem() to Items.EARTH_RUNE,
            18.getPropertyAmount() to 1000,
            19.getPropertyItem() to Items.FIRE_RUNE,
            19.getPropertyAmount() to 1000,

            // Inventory sixth row
            20.getPropertyItem() to Items.MIND_RUNE,
            20.getPropertyAmount() to 4000,

            // Inventory seventh row
            27.getPropertyItem() to Items.COINS_995,
            27.getPropertyAmount() to 10_000_000
    )
}

on_login {
    val newAccount = player.attr[NEW_ACCOUNT_ATTR] ?: return@on_login
    if (newAccount) {
        val items = getStarterItems()
        items.forEach { slotItem ->
            player.inventory.add(item = slotItem.item, beginSlot = slotItem.slot)
        }
    }
}

fun getStarterItems(): List<SlotItem> {
    val items = mutableListOf<SlotItem>()
    for (i in 0 until 28) {
        val item = getProperty<Int>(i.getPropertyItem()) ?: continue
        val amt = getProperty<Int>(i.getPropertyAmount()) ?: 1
        items.add(SlotItem(i, Item(item, amt)))
    }
    return items
}

fun Int.getPropertyItem(): String = "item[$this]"

fun Int.getPropertyAmount(): String = "amount[$this]"