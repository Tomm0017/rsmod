package gg.rsmod.game.model.shop

import gg.rsmod.game.model.PlayerUID
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.CURRENT_SHOP_ATTR
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

/**
 * Represents an in-game shop where items can exchanged.
 *
 * @param name
 * The name of the shop. This name <strong>must</strong> be unique as the shops
 * are stored in a map with the [name] value being its key.
 *
 * @param stockType
 * The [StockType] for the shop.
 *
 * @param purchasePolicy
 * The [PurchasePolicy] when an item is offered to the shop.
 *
 * @param currency
 * The [ShopCurrency] that should be used when exchanging items from and/or to
 * the shop.
 *
 * @param items
 * The [ShopItem]s that are in stock for the shop.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Shop(val name: String, val stockType: StockType, val purchasePolicy: PurchasePolicy,
                val currency: ShopCurrency, val items: Array<ShopItem?>) {

    /**
     * The [gg.rsmod.game.model.entity.Player.uid]s for players who currently have
     * this shop opened on their screen.
     */
    val viewers = ObjectOpenHashSet<PlayerUID>()

    private var currentCycle = 0

    /**
     * Refresh the shop for all [viewers].
     */
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

    fun cycle(world: World) {
        var refresh = false

        for (i in 0 until items.size) {
            val item = items[i] ?: continue
            if (item.currentAmount != item.amount && currentCycle % item.resupplyCycles == 0) {
                val amount = if (item.currentAmount > item.amount) Math.max(item.amount, item.currentAmount - item.resupplyAmount)
                                else Math.min(item.amount, item.currentAmount + item.resupplyAmount)
                /*
                 * When an item's initial [ShopItem.amount] is 0, it means that
                 * the item was not initially in the shop, but was added later.
                 * These items should be removed once they hit a quantity of 0.
                 */
                if (amount == 0 && item.amount == 0) {
                    items[i] = null
                } else {
                    item.currentAmount = amount
                }
                refresh = true
            }
        }

        if (refresh) {
            refresh(world)
        }

        currentCycle++

        if (currentCycle == Int.MAX_VALUE) {
            currentCycle = 0
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

    companion object {
        /**
         * The default amount of items that can be displayed on shops at a time.
         */
        const val DEFAULT_STOCK_SIZE = 40
        /**
         * The default amount of an item that is resupplied per "resupply tick".
         */
        const val DEFAULT_RESUPPLY_AMOUNT = 1
        /**
         * The default amount of cycles per "resupply tick".
         */
        const val DEFAULT_RESUPPLY_CYCLES = 25
    }
}
