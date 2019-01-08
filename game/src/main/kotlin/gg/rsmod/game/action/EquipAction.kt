package gg.rsmod.game.action

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.service.game.ItemStatsService

/**
 * This class is responsible for handling armor equip and unequip related
 * actions.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object EquipAction {

    fun equip(p: Player, item: Item, inventorySlot: Int = -1): Boolean {
        val statService = p.world.getService(ItemStatsService::class.java, searchSubclasses = false).orElse(null)
        if (statService == null) {
            if (!p.world.plugins.executeItem(p, item.id, 2)) {
                p.message("Unhandled item action: [item=${item.id}, slot=$inventorySlot, option=2]")
            }
            return false
        }
        val stats = statService.get(item.id)
        if (stats == null || stats.equipSlot < 0) {
            if (!p.world.plugins.executeItem(p, item.id, 2)) {
                p.message("Unhandled item action: [item=${item.id}, slot=$inventorySlot, option=2]")
            }
            return false
        }

        if (!p.world.plugins.executeEquipItemRequirement(p, item.id)) {
            /**
             * Note(Tom):
             *
             * The equipping was handled, so we return true. This is to avoid
             * the message giving us an "Unhandled item action" debug message.
             *
             * Though, this could sort of return false-positives when you want
             * to equip an item on a player and use this to see whether or not
             * they are wearing the item now. We could either return an enum state
             * instead of a boolean or that piece of code that needs to see if the
             * player is wearing the item can just use [Player.equipment] to check
             * if they are wearing it, instead.
             */
            return true
        }

        val equipSlot = stats.equipSlot
        val equipType = stats.equipType

        val newItem = Item(item)

        /**
         * A list of equipment slots that should be unequipped when [item] is
         * equipped.
         */
        val unequip = arrayListOf(equipSlot)
        if (equipType != 0) {
            /**
             * [gg.rsmod.game.model.item.ItemStats.equipType] counts as a 'secondary'
             * equipment slot, which should be unequipped as well.
             *
             * For example, 2h swords have an equipment type of 5, which is also
             * the equipment slot of shields.
             */
            unequip.add(equipType)
        }

        /**
         * We check the already-equipped items and see if any of them have an
         * equipment *type* equal to the equipment slot of [item]. This is so
         * that items like shields will make sure 2h swords are unequipped even
         * though shields don't have a 'secondary' equipment slot (equipment type).
         */
        for (i in 0 until p.equipment.capacity) {
            val equip = p.equipment[i] ?: continue
            val otherStats = statService.get(equip.id) ?: continue
            if (otherStats.equipType == equipSlot) {
                unequip.add(i)
            }
        }

        val spaceRequired = unequip.filter { slot -> p.equipment[slot] != null }.size
        if (p.inventory.getFreeSlotCount() < spaceRequired) {
            p.message("You don't have enough free inventory space to do that.")
            return false
        }

        if (inventorySlot == -1 || p.inventory.remove(item.id, beginSlot = inventorySlot).hasSucceeded()) {
            unequip.forEach { slot ->
                val equipment = p.equipment[slot] ?: return@forEach
                val equipmentId = equipment.id

                val transaction = p.inventory.add(equipment.id, equipment.amount, beginSlot = if (inventorySlot != -1) inventorySlot else 0)
                transaction.items.first().copyAttr(equipment)

                /**
                 * The item in [equipSlot] will be removed afterwards, so don't
                 * remove it here!
                 */
                if (slot != equipSlot) {
                    p.equipment.set(slot, null)
                }
                p.world.plugins.executeUnequipItem(p, equipmentId)
            }

            p.equipment.set(equipSlot, newItem)
            p.world.plugins.executeEquipSlot(p, equipSlot)
            p.world.plugins.executeEquipItem(p, newItem.id)
        }
        return true
    }

    fun unequip(p: Player, equipmentSlot: Int): Boolean {
        val item = p.equipment[equipmentSlot] ?: return false

        check(item.amount > 0)

        //you don't have enough free space to do that. (try to remove from equipment directly)

        return true
    }
}