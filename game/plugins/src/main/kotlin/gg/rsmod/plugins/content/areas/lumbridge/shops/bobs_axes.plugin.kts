package gg.rsmod.plugins.content.areas.lumbridge.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Bob's Brilliant Axes.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BRONZE_PICKAXE, 5, 1, 0)
    items[1] = ShopItem(Items.BRONZE_AXE, 10, 16, 9)
    items[2] = ShopItem(Items.IRON_AXE, 5, 56, 33)
    items[3] = ShopItem(Items.STEEL_AXE, 3, 200, 120)
    items[4] = ShopItem(Items.IRON_BATTLEAXE, 5, 182, 109)
    items[5] = ShopItem(Items.STEEL_BATTLEAXE, 2, 650,390)
    items[6] = ShopItem(Items.MITHRIL_BATTLEAXE, 1, 1690, 1014)
}