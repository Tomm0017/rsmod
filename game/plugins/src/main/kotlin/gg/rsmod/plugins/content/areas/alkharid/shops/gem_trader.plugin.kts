package gg.rsmod.plugins.content.areas.alkharid.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Gem Trader.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.UNCUT_SAPPHIRE, 1)
    items[1] = ShopItem(Items.UNCUT_EMERALD, 1)
    items[2] = ShopItem(Items.UNCUT_RUBY, 0)
    items[3] = ShopItem(Items.UNCUT_DIAMOND, 0)
    items[4] = ShopItem(Items.SAPPHIRE, 1)
    items[5] = ShopItem(Items.EMERALD, 1)
    items[6] = ShopItem(Items.RUBY, 0)
    items[7] = ShopItem(Items.DIAMOND, 0)
}