package gg.rsmod.plugins.content.areas.lumbridge.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Lumbridge General Store", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_TRADEABLES) {
    items[0] = ShopItem(Items.POT, 5, 1, 0)
    items[1] = ShopItem(Items.JUG, 2, 1, 0)
    items[2] = ShopItem(Items.EMPTY_JUG_PACK, 5, 182, 56)
    items[3] = ShopItem(Items.SHEARS, 2, 1, 0)
    items[4] = ShopItem(Items.BUCKET, 3, 2, 0)
    items[5] = ShopItem(Items.EMPTY_BUCKET_PACK, 15, 650,200)
    items[6] = ShopItem(Items.BOWL, 2, 5, 1)
    items[7] = ShopItem(Items.CAKE_TIN, 2, 13, 4)
    items[8] = ShopItem(Items.TINDERBOX, 2, 1, 0)
    items[9] = ShopItem(Items.CHISEL, 2, 1, 0)
    items[10] = ShopItem(952, 5, 3, 1)
    items[11] = ShopItem(Items.HAMMER, 5, 1, 0)
    items[12] = ShopItem(Items.NEWCOMER_MAP, 5, 1, 0)
    items[13] = ShopItem(Items.SECURITY_BOOK, 5, 2, 0)
}