package gg.rsmod.plugins.content.areas.edgeville.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Richard's Wilderness Cape Shop.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.TEAM6_CAPE, 100)
    items[1] = ShopItem(Items.TEAM16_CAPE, 100)
    items[2] = ShopItem(Items.TEAM26_CAPE, 100)
    items[3] = ShopItem(Items.TEAM36_CAPE, 100)
    items[4] = ShopItem(Items.TEAM46_CAPE, 100)
}