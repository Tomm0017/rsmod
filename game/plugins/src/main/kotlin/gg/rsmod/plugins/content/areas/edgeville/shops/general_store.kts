package gg.rsmod.plugins.content.areas.edgeville.shops

import gg.rsmod.game.model.shop.ShopItem
import gg.rsmod.plugins.api.cfg.Items

set_shop("Edgeville General Store") {
    it.items[0] = ShopItem(item = Items.POT, amount = 5)
    it.items[1] = ShopItem(item = Items.JUG, amount = 2)
    it.items[2] = ShopItem(item = Items.EMPTY_JUG_PACK, amount = 5)
    it.items[3] = ShopItem(item = Items.SHEARS, amount = 2)
    it.items[4] = ShopItem(item = Items.BUCKET, amount = 3)
    it.items[5] = ShopItem(item = Items.EMPTY_BUCKET_PACK, amount = 15)
    it.items[6] = ShopItem(item = Items.BOWL, amount = 2)
    it.items[7] = ShopItem(item = Items.CAKE_TIN, amount = 2)
    it.items[8] = ShopItem(item = Items.TINDERBOX, amount = 2)
    it.items[9] = ShopItem(item = Items.CHISEL, amount = 2)
    it.items[10] = ShopItem(item = Items.HAMMER, amount = 5)
    it.items[11] = ShopItem(item = Items.NEWCOMER_MAP, amount = 5)
    it.items[12] = ShopItem(item = Items.SECURITY_BOOK, amount = 5)
}