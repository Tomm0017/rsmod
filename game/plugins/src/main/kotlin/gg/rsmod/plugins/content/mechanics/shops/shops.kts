package gg.rsmod.plugins.content.mechanics.shops

import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.attr.CURRENT_SHOP_ATTR
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.shop.Shop
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*

val PURCHASE_OPTS = arrayOf(1, 5, 10, 50)

val SHOP_INTERFACE_ID = 300
val INV_INTERFACE_ID = 301

val OPTION_1 = "Value"
val OPTION_2 = "Sell ${PURCHASE_OPTS[0]}"
val OPTION_3 = "Sell ${PURCHASE_OPTS[1]}"
val OPTION_4 = "Sell ${PURCHASE_OPTS[2]}"
val OPTION_5 = "Sell ${PURCHASE_OPTS[3]}"

on_interface_open(interfaceId = SHOP_INTERFACE_ID) {
    val player = it.player()
    it.player().attr[CURRENT_SHOP_ATTR]?.let { shop ->
        player.runClientScript(149, 19726336, 93, 4, 7, 0, -1, "$OPTION_1<col=ff9040>", "$OPTION_2<col=ff9040>", "$OPTION_3<col=ff9040>", "$OPTION_4<col=ff9040>", "$OPTION_5<col=ff9040>")
        shop.viewers.add(player.uid!!)
    }
}

on_interface_close(interfaceId = SHOP_INTERFACE_ID) {
    val player = it.player()
    it.player().attr[CURRENT_SHOP_ATTR]?.let { shop ->
        shop.viewers.remove(player.uid!!)
        player.closeInterface(interfaceId = INV_INTERFACE_ID)
        player.openInterface(dest = InterfaceDestination.INVENTORY)
    }
}

on_button(interfaceId = SHOP_INTERFACE_ID, component = 16) {
    val player = it.player()
    player.attr[CURRENT_SHOP_ATTR]?.let { shop ->
        val opt = it.getInteractingOption()
        val slot = it.getInteractingSlot()
        val shopItem = shop.items[slot] ?: return@on_button

        if (opt == 1) {
            val item = Item(shopItem.item)
            val value = shop.getSellPrice(player.world, item.id)
            val name = item.getName(player.world)
            player.message("$name: currently costs ${value.appendToString("coin")}")
        } else if (opt == 10) {
            player.world.sendExamine(player, shopItem.item, ExamineEntityType.ITEM)
        } else {
            val amount = when (opt) {
                2 -> PURCHASE_OPTS[0]
                3 -> PURCHASE_OPTS[1]
                4 -> PURCHASE_OPTS[2]
                5 -> PURCHASE_OPTS[3]
                else -> return@on_button
            }
            buy_item(player, shop, slot, amount)
        }
    }
}

fun buy_item(p: Player, shop: Shop, item: Int, amt: Int) {
    // You don't have enough inventory space.
    // The shop has run out of stock.
    val cost = shop.getSellPrice(p.world, item)


}