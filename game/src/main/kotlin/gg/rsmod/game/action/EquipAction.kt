package gg.rsmod.game.action

import gg.rsmod.game.fs.def.ItemDef
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

    /**
     * All possible results when trying to equip or unequip an item.
     */
    enum class Result {
        /**
         * The item could not be equipped.
         */
        UNHANDLED,
        /**
         * The item equip was handled by plugins, i.e a requirement was not met.
         */
        PLUGIN,
        /**
         * No free space, either in inventory or equipment, to equip item.
         */
        NO_FREE_SPACE,
        /**
         * The item was equipped or unequipped successfully.
         */
        SUCCESS,
        /**
         * Item interaction could not be handled.
         */
        INVALID_ITEM
    }

    fun equip(p: Player, item: Item, inventorySlot: Int = -1): Result {
        val statService = p.world.getService(ItemStatsService::class.java).orElse(null)
        if (statService == null) {
            if (p.world.plugins.executeItem(p, item.id, 2)) {
                return Result.PLUGIN
            }
            return Result.UNHANDLED
        }
        val stats = statService.get(item.id)
        if (stats == null || stats.equipSlot < 0) {
            if (p.world.plugins.executeItem(p, item.id, 2)) {
                return Result.PLUGIN
            }
            return Result.UNHANDLED
        }

        if (!p.world.plugins.executeEquipItemRequirement(p, item.id)) {
            return Result.PLUGIN
        }

        val equipSlot = stats.equipSlot
        val equipType = stats.equipType

        val replace = p.equipment[equipSlot]
        val itemDef = p.world.definitions.get(ItemDef::class.java, item.id)
        val stackable = itemDef.isStackable()

        /**
         * If [item] is stackable and the player has the item equipped already,
         * we add the amount as much as we can.
         */
        if (stackable && replace?.id == item.id) {
            val add = Math.min(item.amount, Int.MAX_VALUE - replace.amount)
            if (add <= 0) {
                p.message("You don't have enough free equipment space to do that.")
                return Result.NO_FREE_SPACE
            }
            if (inventorySlot != -1) {
                val transaction = p.inventory.remove(item.id, amount = add, assureFullRemoval = false, beginSlot = inventorySlot)
                if (transaction.completed == 0) {
                    return Result.INVALID_ITEM
                }
                p.equipment.set(equipSlot, Item(replace.id, transaction.completed + replace.amount))
            } else {
                p.equipment.set(equipSlot, Item(replace.id, add + replace.amount))
            }
            p.world.plugins.executeEquipSlot(p, equipSlot)
            p.world.plugins.executeEquipItem(p, replace.id)
        } else {
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
                if (otherStats.equipType == equipSlot && otherStats.equipType != 0) {
                    unequip.add(i)
                }
            }

            val spaceRequired = unequip.filter { slot -> p.equipment[slot] != null }.size - 1
            if (p.inventory.getFreeSlotCount() < spaceRequired) {
                p.message("You don't have enough free inventory space to do that.")
                return Result.NO_FREE_SPACE
            }

            val newEquippedItem = Item(item)

            if (inventorySlot == -1 || p.inventory.remove(item.id, item.amount, beginSlot = inventorySlot).hasSucceeded()) {
                var initialSlot = inventorySlot

                /**
                 * If the item being equipped it stackable and replacing an item
                 * already equipped, we want to see if the item being replaced already
                 * occupies a space in the player's inventory - if so we want to add
                 * the item to that stack if possible. If the stack is too big, we
                 * don't equip the new item and let the player know of this issue.
                 */
                if (stackable && replace != null) {
                    val maxAmount = Int.MAX_VALUE - p.inventory.getItemCount(replace.id)
                    if (replace.amount > maxAmount) {
                        if (inventorySlot != -1) {
                            p.inventory.add(item.id, newEquippedItem.amount, beginSlot = inventorySlot)
                        }
                        p.message("You don't have enough free inventory space to do that.")
                        return Result.NO_FREE_SPACE
                    }
                    if (maxAmount != Int.MAX_VALUE) {
                        initialSlot = p.inventory.getItemIndex(replace.id, skipAttrItems = true)
                    }
                }

                /**
                 * Unequip any overlapping items.
                 */
                unequip.forEach { slot ->
                    val equipment = p.equipment[slot] ?: return@forEach
                    val equipmentId = equipment.id

                    val transaction = p.inventory.add(equipment.id, equipment.amount, beginSlot = if (initialSlot != -1) initialSlot else 0)
                    transaction.items.firstOrNull()?.item?.copyAttr(equipment)
                    initialSlot = -1

                    /**
                     * The item in equipSlot will be removed afterwards, so don't
                     * remove it here!
                     */
                    if (slot != equipSlot) {
                        p.equipment.set(slot, null)
                    }
                    p.world.plugins.executeUnequipItem(p, equipmentId)
                }

                p.equipment.set(equipSlot, newEquippedItem)
                p.world.plugins.executeEquipSlot(p, equipSlot)
                p.world.plugins.executeEquipItem(p, newEquippedItem.id)
            }
        }
        return Result.SUCCESS
    }

    fun unequip(p: Player, equipmentSlot: Int): Result {
        val item = p.equipment[equipmentSlot] ?: return Result.INVALID_ITEM

        val addition = p.inventory.add(item.id, item.amount, assureFullInsertion = false)

        if (addition.completed == 0) {
            p.message("You don't have enough free space to do that.")
            return Result.NO_FREE_SPACE
        }

        if (addition.getLeftOver() == 0) {
            p.equipment.set(equipmentSlot, null)
        } else {
            val leftover = Item(item, addition.getLeftOver())
            p.equipment.set(equipmentSlot, leftover)
        }
        return Result.SUCCESS
    }
}