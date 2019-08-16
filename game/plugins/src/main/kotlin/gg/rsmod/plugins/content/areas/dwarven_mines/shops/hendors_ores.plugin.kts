package gg.rsmod.plugins.content.areas.dwarven_mines.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Hendor's Awesome Ores", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.COPPER_ORE, 0)
    items[1] = ShopItem(Items.TIN_ORE, 0)
    items[2] = ShopItem(Items.IRON_ORE, 0)
    items[3] = ShopItem(Items.MITHRIL_ORE, 0)
    items[4] = ShopItem(Items.ADAMANTITE_ORE, 0)
    items[5] = ShopItem(Items.RUNITE_ORE, 0)
    items[6] = ShopItem(Items.COAL, 0)
}