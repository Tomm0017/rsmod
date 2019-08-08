package gg.rsmod.plugins.content.areas.alkharid.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Louie's Armoured Legs Bazaar.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BRONZE_PLATELEGS, 5)
    items[1] = ShopItem(Items.IRON_PLATELEGS, 3)
    items[2] = ShopItem(Items.STEEL_PLATELEGS, 2)
    items[3] = ShopItem(Items.BLACK_PLATELEGS, 1)
    items[4] = ShopItem(Items.MITHRIL_PLATELEGS, 1)
    items[5] = ShopItem(Items.ADAMANT_PLATELEGS, 1)
}