package gg.rsmod.plugins.content.areas.alkharid.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Zeke's Superior Scimitars.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BRONZE_SCIMITAR, 5)
    items[1] = ShopItem(Items.IRON_SCIMITAR, 3)
    items[2] = ShopItem(Items.STEEL_SCIMITAR, 2)
    items[4] = ShopItem(Items.MITHRIL_SCIMITAR, 1)
}