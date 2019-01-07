package gg.rsmod.game.action

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.service.game.ItemStatsService
import gg.rsmod.game.sync.UpdateBlockType

/**
 * @author Tom <rspsmods@gmail.com>
 */
object EquipAction {

    fun equip(player: Player, item: Item, inventorySlot: Int = -1): Boolean {
        val statService = player.world.getService(ItemStatsService::class.java, searchSubclasses = false).orElse(null)
        if (statService == null) {
            player.world.plugins.executeItem(player, item.id, 2)
            return false
        }
        val stats = statService.get(item.id)
        if (stats == null) {
            player.world.plugins.executeItem(player, item.id, 2)
            return false
        }

        val equipSlot = stats.equipSlot
        val equipType = stats.equipType

        val oldItem = player.equipment[equipSlot]
        val newItem = Item(item)

        // TODO(Tom): still need further logic for things like 2h swords
        if (inventorySlot == -1 || player.inventory.remove(item.id, beginSlot = inventorySlot).hasSucceeded()) {
            if (oldItem != null) {
                val transaction = player.inventory.add(oldItem.id, oldItem.amount, beginSlot = if (inventorySlot != -1) inventorySlot else 0)
                transaction.items[0].copyAttr(oldItem)
            }
            player.equipment.set(equipSlot, newItem)
            player.addBlock(UpdateBlockType.APPEARANCE)
        }
        return true
    }
}