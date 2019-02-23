package gg.rsmod.game.model.shop

import gg.rsmod.game.model.PlayerUID
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.CURRENT_SHOP_ATTR
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Shop(val name: String, val stockType: StockType, val purchasePolicy: PurchasePolicy,
                val currency: ShopCurrency, val items: Array<ShopItem?>) {

    companion object {
        const val DEFAULT_STOCK_SIZE = 28
        const val DEFAULT_RESUPPLY_AMOUNT = 1
        const val DEFAULT_RESUPPLY_CYCLES = 25
    }

    val viewers = ObjectOpenHashSet<PlayerUID>()

    fun refresh(world: World) {
        val iterator = viewers.iterator()
        while (iterator.hasNext()) {
            val viewer = iterator.next()
            val player = world.getPlayerForUid(viewer)
            if (player == null) {
                iterator.remove()
                continue
            }
            if (player.attr[CURRENT_SHOP_ATTR] == this) {
                player.shopDirty = true
            } else {
                iterator.remove()
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Shop

        if (name != other.name) return false
        if (stockType != other.stockType) return false
        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + stockType.hashCode()
        result = 31 * result + items.contentHashCode()
        return result
    }
}