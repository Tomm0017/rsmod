package gg.rsmod.plugins.content.areas.varrock.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Aubury's Rune Shop.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.FIRE_RUNE, 5000)
    items[1] = ShopItem(Items.WATER_RUNE, 5000)
    items[2] = ShopItem(Items.AIR_RUNE, 5000)
    items[3] = ShopItem(Items.EARTH_RUNE, 5000)
    items[4] = ShopItem(Items.MIND_RUNE, 5000)
    items[5] = ShopItem(Items.BODY_RUNE, 5000)
    items[6] = ShopItem(Items.CHAOS_RUNE, 250)
    items[7] = ShopItem(Items.DEATH_RUNE, 250)
    items[8] = ShopItem(Items.FIRE_RUNE_PACK, 80)
    items[9] = ShopItem(Items.WATER_RUNE_PACK, 80)
    items[10] = ShopItem(Items.AIR_RUNE_PACK, 80)
    items[11] = ShopItem(Items.EARTH_RUNE_PACK, 80)
    items[12] = ShopItem(Items.MIND_RUNE_PACK, 40)
    items[13] = ShopItem(Items.CHAOS_RUNE_PACK, 35)
}