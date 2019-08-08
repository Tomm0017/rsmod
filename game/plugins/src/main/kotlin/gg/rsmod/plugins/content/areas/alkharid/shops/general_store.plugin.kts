package gg.rsmod.plugins.content.areas.alkharid.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Al-Kharid General Store", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_TRADEABLES) {
    items[0] = ShopItem(Items.POT, 5)
    items[1] = ShopItem(Items.JUG, 2)
    items[2] = ShopItem(Items.EMPTY_JUG_PACK, 5)
    items[3] = ShopItem(Items.SHEARS, 2)
    items[4] = ShopItem(Items.BUCKET, 3)
    items[5] = ShopItem(Items.EMPTY_BUCKET_PACK, 15)
    items[6] = ShopItem(Items.BOWL, 2)
    items[7] = ShopItem(Items.CAKE_TIN, 2)
    items[8] = ShopItem(Items.TINDERBOX, 2)
    items[9] = ShopItem(Items.CHISEL, 2)
    items[10] = ShopItem(Items.HAMMER, 5)
    items[11] = ShopItem(Items.NEWCOMER_MAP, 5)
    items[12] = ShopItem(Items.SECURITY_BOOK, 5)
}