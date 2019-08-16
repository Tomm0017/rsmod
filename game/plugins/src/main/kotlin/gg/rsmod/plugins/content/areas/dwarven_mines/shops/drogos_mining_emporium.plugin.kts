package gg.rsmod.plugins.content.areas.dwarven_mines.shops

import gg.rsmod.plugins.content.mechanics.shops.CoinCurrency

create_shop("Drogo's Mining Emporium.", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.HAMMER, 4)
    items[1] = ShopItem(Items.BRONZE_PICKAXE, 4)
    items[2] = ShopItem(Items.COPPER_ORE, 0)
    items[3] = ShopItem(Items.TIN_ORE, 0)
    items[4] = ShopItem(Items.IRON_ORE, 0)
    items[5] = ShopItem(Items.COAL, 0)
    items[6] = ShopItem(Items.BRONZE_BAR, 0)
    items[7] = ShopItem(Items.IRON_BAR, 0)
    items[8] = ShopItem(Items.GOLD_BAR, 0)
}