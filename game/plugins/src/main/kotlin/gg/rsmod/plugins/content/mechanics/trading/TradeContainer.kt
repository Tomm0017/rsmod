package gg.rsmod.plugins.content.mechanics.trading

import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.api.ext.sendItemContainer

class TradeContainer(private val player: Player) {

    private val tradeOffer = ItemContainer(player.world.definitions, player.inventory.capacity, ContainerStackType.NORMAL)
    private val inventory = ItemContainer(player.inventory)

    private var onItemOffered : ((Item) -> Unit)? = null

    fun offer(slot: Int, amount: Int) {
        val item = player.inventory.get(slot) ?: return
        onItemOffered?.invoke(item)
    }

    fun bindItemOffered(onItemOffered: (Item) -> Unit) {
        this.onItemOffered = onItemOffered
    }
}