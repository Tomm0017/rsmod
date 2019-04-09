package gg.rsmod.plugins.content.mechanics.shops

import gg.rsmod.game.model.attr.CURRENT_SHOP_ATTR

val SHOP_INTERFACE_ID = 300
val INV_INTERFACE_ID = 301

val BUY_OPTS = arrayOf(1, 5, 10, 50)
val SELL_OPTS = arrayOf(1, 5, 10, 50)

val OPTION_1 = "Value"
val OPTION_2 = "Sell ${BUY_OPTS[0]}"
val OPTION_3 = "Sell ${BUY_OPTS[1]}"
val OPTION_4 = "Sell ${BUY_OPTS[2]}"
val OPTION_5 = "Sell ${BUY_OPTS[3]}"

on_interface_open(interfaceId = SHOP_INTERFACE_ID) {
    player.attr[CURRENT_SHOP_ATTR]?.let { shop ->
        player.runClientScript(149, 19726336, 93, 4, 7, 0, -1, "$OPTION_1<col=ff9040>", "$OPTION_2<col=ff9040>", "$OPTION_3<col=ff9040>", "$OPTION_4<col=ff9040>", "$OPTION_5<col=ff9040>")
        shop.viewers.add(player.uid)
    }
}

on_interface_close(interfaceId = SHOP_INTERFACE_ID) {
    player.attr[CURRENT_SHOP_ATTR]?.let { shop ->
        shop.viewers.remove(player.uid)
        player.closeInterface(interfaceId = INV_INTERFACE_ID)
        player.openInterface(dest = InterfaceDestination.INVENTORY)
    }
}

on_button(interfaceId = SHOP_INTERFACE_ID, component = 16) {
    player.attr[CURRENT_SHOP_ATTR]?.let { shop ->
        val opt = player.getInteractingOption()
        val slot = player.getInteractingSlot() - 1
        val shopItem = shop.items[slot] ?: return@on_button

        when (opt) {
            1 -> shop.currency.onSellValueMessage(player, shopItem)
            10 -> world.sendExamine(player, shopItem.item, ExamineEntityType.ITEM)
            else -> {
                val amount = when (opt) {
                    2 -> BUY_OPTS[0]
                    3 -> BUY_OPTS[1]
                    4 -> BUY_OPTS[2]
                    5 -> BUY_OPTS[3]
                    else -> return@on_button
                }
                shop.currency.sellToPlayer(player, shop, slot, amount)
            }
        }
    }
}

on_button(interfaceId = INV_INTERFACE_ID, component = 0) {
    player.attr[CURRENT_SHOP_ATTR]?.let { shop ->
        val opt = player.getInteractingOption()
        val slot = player.getInteractingSlot()
        val item = player.inventory[slot] ?: return@on_button

        when (opt) {
            1 -> shop.currency.onBuyValueMessage(player, shop, item.id)
            10 -> world.sendExamine(player, item.id, ExamineEntityType.ITEM)
            else -> {
                val amount = when (opt) {
                    2 -> SELL_OPTS[0]
                    3 -> SELL_OPTS[1]
                    4 -> SELL_OPTS[2]
                    5 -> SELL_OPTS[3]
                    else -> return@on_button
                }
                shop.currency.buyFromPlayer(player, shop, slot, amount)
            }
        }
    }
}