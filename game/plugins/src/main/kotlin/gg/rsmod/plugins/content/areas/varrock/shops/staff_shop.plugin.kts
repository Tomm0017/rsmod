package gg.rsmod.plugins.content.areas.varrock.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Zaff's Superior Staffs!", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BATTLESTAFF, 5)
    items[1] = ShopItem(Items.STAFF, 5)
    items[2] = ShopItem(Items.MAGIC_STAFF, 5)
    items[3] = ShopItem(Items.STAFF_OF_AIR, 2)
    items[4] = ShopItem(Items.STAFF_OF_WATER, 2)
    items[5] = ShopItem(Items.STAFF_OF_EARTH, 2)
    items[6] = ShopItem(Items.STAFF_OF_FIRE, 2)
}