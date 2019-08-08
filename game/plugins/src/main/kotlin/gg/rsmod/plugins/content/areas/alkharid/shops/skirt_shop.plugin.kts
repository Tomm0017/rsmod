package gg.rsmod.plugins.content.areas.alkharid.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Ranael's Super Skirt Store.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BRONZE_PLATESKIRT, 5)
    items[1] = ShopItem(Items.IRON_PLATESKIRT, 3)
    items[2] = ShopItem(Items.STEEL_PLATESKIRT, 2)
    items[3] = ShopItem(Items.BLACK_PLATESKIRT, 1)
    items[4] = ShopItem(Items.MITHRIL_PLATESKIRT, 1)
    items[5] = ShopItem(Items.ADAMANT_PLATESKIRT, 1)
}