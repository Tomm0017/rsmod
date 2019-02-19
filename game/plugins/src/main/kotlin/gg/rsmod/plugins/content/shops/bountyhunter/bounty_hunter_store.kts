package gg.rsmod.plugins.content.shops.bountyhunter

import gg.rsmod.game.model.shop.PurchasePolicy
import gg.rsmod.game.model.shop.ShopItem
import gg.rsmod.game.model.shop.StockType
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.mechanics.shops.ItemCurrency

create_shop("Bounty Hunter Store", ItemCurrency(currencyItem = Items.BLOOD_MONEY, singularCurrency = "Blood Money", pluralCurrency = "Blood Money"),
        stockType = StockType.INFINITE, purchasePolicy = PurchasePolicy.BUY_NONE) {
    items[0] = ShopItem(Items.DRAGON_SCIMITAR, 1, sellPrice = 300_000)
}