package gg.rsmod.plugins.content.areas.alkharid.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Dommik's Crafting Store.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.CHISEL, 2)
    items[1] = ShopItem(Items.RING_MOULD, 10)
    items[2] = ShopItem(Items.NECKLACE_MOULD, 2)
    items[3] = ShopItem(Items.NEEDLE, 3)
    items[4] = ShopItem(Items.THREAD, 100)
    items[5] = ShopItem(Items.HOLY_MOULD, 3)
    items[6] = ShopItem(Items.SICKLE_MOULD, 10)
    items[7] = ShopItem(Items.TIARA_MOULD, 10)
    items[8] = ShopItem(Items.BOLT_MOULD, 10)
    items[9] = ShopItem(Items.BRACELET_MOULD, 5)

}