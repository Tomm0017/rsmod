package gg.rsmod.plugins.content.areas.dwarven_mines.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Dwarven shopping store", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_TRADEABLES) {
    items[0] = ShopItem(Items.POT, 3)
    items[1] = ShopItem(Items.JUG, 2)
    items[2] = ShopItem(Items.EMPTY_JUG_PACK, 5)
    items[3] = ShopItem(Items.SHEARS, 2)
    items[4] = ShopItem(Items.BUCKET, 3)
    items[5] = ShopItem(Items.EMPTY_BUCKET_PACK, 15)
    items[6] = ShopItem(Items.TINDERBOX, 2)
    items[7] = ShopItem(Items.CHISEL, 2)
    items[8] = ShopItem(Items.HAMMER, 5)
}