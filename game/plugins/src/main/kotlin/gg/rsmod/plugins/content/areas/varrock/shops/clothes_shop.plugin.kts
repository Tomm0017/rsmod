package gg.rsmod.plugins.content.areas.varrock.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Thessalia's Fine Clothes.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.WHITE_APRON, 3)
    items[1] = ShopItem(Items.LEATHER_BODY, 12)
    items[2] = ShopItem(Items.LEATHER_GLOVES, 10)
    items[3] = ShopItem(Items.LEATHER_BOOTS, 10)
    items[4] = ShopItem(Items.BROWN_APRON, 1)
    items[5] = ShopItem(Items.PINK_SKIRT, 5)
    items[6] = ShopItem(Items.BLACK_SKIRT, 3)
    items[7] = ShopItem(Items.BLUE_SKIRT, 2)
    items[8] = ShopItem(Items.RED_CAPE, 4)
    items[9] = ShopItem(Items.SILK, 5)
    items[10] = ShopItem(Items.PRIEST_GOWN_428, 3)
    items[11] = ShopItem(Items.PRIEST_GOWN, 3)
}