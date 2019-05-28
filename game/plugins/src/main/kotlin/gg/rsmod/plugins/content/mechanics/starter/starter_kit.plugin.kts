package gg.rsmod.plugins.content.mechanics.starter

import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR

load_metadata {
    propertyFileName = "starter_kit"

    author = "Tomm"
    name = "Starter Kit"
    description = "Give items to new accounts."

    properties(
            // Inventory first row
            0.getItem to Items.IRON_FULL_HELM,
            1.getItem to Items.IRON_PLATEBODY,
            2.getItem to Items.IRON_PLATELEGS,
            3.getItem to Items.IRON_SCIMITAR,

            // Inventory second row
            4.getItem to Items.BLUE_WIZARD_HAT,
            5.getItem to Items.BLUE_WIZARD_ROBE,
            6.getItem to Items.BLUE_SKIRT,
            7.getItem to Items.STAFF_OF_AIR,

            // Inventory third row
            8.getItem to Items.LEATHER_COWL,
            9.getItem to Items.LEATHER_BODY,
            10.getItem to Items.LEATHER_CHAPS,
            11.getItem to Items.SHORTBOW,

            // Inventory fourth row
            12.getItem to Items.AMULET_OF_POWER,
            13.getItem to Items.LEATHER_GLOVES,
            14.getItem to Items.CLIMBING_BOOTS,
            15.getItem to Items.IRON_ARROW,
            15.getItemAmount to 1000,

            // Inventory fifth row
            16.getItem to Items.AIR_RUNE,
            16.getItemAmount to 1000,
            17.getItem to Items.WATER_RUNE,
            17.getItemAmount to 1000,
            18.getItem to Items.EARTH_RUNE,
            18.getItemAmount to 1000,
            19.getItem to Items.FIRE_RUNE,
            19.getItemAmount to 1000,

            // Inventory sixth row
            20.getItem to Items.MIND_RUNE,
            20.getItemAmount to 4000,

            // Inventory seventh row
            27.getItem to Items.COINS_995,
            27.getItemAmount to 10_000_000
    )
}

on_login {
    val newAccount = player.attr[NEW_ACCOUNT_ATTR] ?: return@on_login
    if (newAccount) {
        val inventory = player.getInventoryStarterItems()
        val bank = player.getBankStarterItems()

        inventory.forEach { slotItem ->
            player.inventory.add(item = slotItem.item, beginSlot = slotItem.slot)
        }

        bank.forEach { slotItem ->
            player.bank.add(item = slotItem.item, beginSlot = slotItem.slot)
        }
    }
}

fun Player.getInventoryStarterItems() = getStarterItems(inventory.capacity, { getItem }, { getItemAmount })

fun Player.getBankStarterItems() = getStarterItems(bank.capacity, { getBankItem }, { getBankItemAmount })

fun getStarterItems(containerCapacity: Int, itemProperty: (Int).() -> String, amountProperty: (Int).() -> String): List<SlotItem> {
    val items = mutableListOf<SlotItem>()
    for (i in 0 until containerCapacity) {
        val item = getProperty<Int>(itemProperty(i)) ?: continue
        val amt = getProperty<Int>(amountProperty(i)) ?: 1
        items.add(SlotItem(i, Item(item, amt)))
    }
    return items
}

val Int.getItem: String
    get() = "item[$this]"

val Int.getItemAmount: String
    get() = "amount[$this]"

val Int.getBankItem: String
    get() = "bank_item[$this]"

val Int.getBankItemAmount: String
    get() = "bank_amount[$this]"