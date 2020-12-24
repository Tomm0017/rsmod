package gg.rsmod.plugins.content.inter.bank

import gg.rsmod.game.model.World
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.inter.bank.BankTabs.insertionPoint
import gg.rsmod.plugins.content.inter.bank.BankTabs.SELECTED_TAB_VARBIT
import gg.rsmod.plugins.content.inter.bank.BankTabs.BANK_TAB_ROOT_VARBIT

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Bank {
    const val BANK_INTERFACE_ID = 12
    const val BANK_MAINTAB_COMPONENT = 12
    const val INV_INTERFACE_ID = 15
    const val INV_INTERFACE_CHILD = 3

    const val WITHDRAW_AS_VARBIT = 3958
    const val REARRANGE_MODE_VARBIT = 3959
    const val ALWAYS_PLACEHOLD_VARBIT = 3755
    const val LAST_X_INPUT = 3960
    const val QUANTITY_VARBIT = 6590
    const val INCINERATOR_VARBIT = 5102


    /**
     * Visual varbit for the "Bank your loot" tab area interface when storing
     * items from a looting bag into the bank.
     */
    private const val BANK_YOUR_LOOT_VARBIT = 4139

    fun withdraw(p: Player, id: Int, amt: Int, slot: Int, placehold: Boolean) {
        var withdrawn = 0

        val from = p.bank
        val to = p.inventory

        val amount = Math.min(from.getItemCount(id), amt)
        val note = p.getVarbit(WITHDRAW_AS_VARBIT) == 1

        for (i in slot until from.capacity) {
            val item = from[i] ?: continue
            if (item.id != id) {
                continue
            }

            if (withdrawn >= amount) {
                break
            }

            val left = amount - withdrawn

            val copy = Item(item.id, Math.min(left, item.amount))
            if (copy.amount >= item.amount) {
                copy.copyAttr(item)
            }

            val transfer = from.transfer(to, item = copy, fromSlot = i, note = note, unnote = !note)
            withdrawn += transfer?.completed ?: 0

            if (from[i] == null) {
                if (placehold || p.getVarbit(ALWAYS_PLACEHOLD_VARBIT) == 1) {
                    val def = item.getDef(p.world.definitions)

                    /**
                     * Make sure the item has a valid placeholder item in its
                     * definition.
                     */
                    if (def.placeholderLink > 0) {
                        p.bank[i] = Item(def.placeholderLink, 0)
                    }
                }
            }
        }

        if (withdrawn == 0) {
            p.message("You don't have enough inventory space.")
        } else if (withdrawn != amount) {
            p.message("You don't have enough inventory space to withdraw that many.")
        }
    }

    fun deposit(p: Player, id: Int, amt: Int) {
        val from = p.inventory
        val to = p.bank

        val oldFree = to.freeSlotCount
        val curTab = p.getVarbit(SELECTED_TAB_VARBIT)

        val amount = Math.min(from.getItemCount(id), amt)

        var deposited = 0
        for (i in 0 until from.capacity) {
            val item = from[i] ?: continue
            if (item.id != id) {
                continue
            }

            if (deposited >= amount) {
                break
            }

            val left = amount - deposited

            val copy = Item(item.id, Math.min(left, item.amount))
            if (copy.amount >= item.amount) {
                copy.copyAttr(item)
            }

            val placeholderSlot = to.removePlaceholder(p.world, copy)
            val transfer = from.transfer(to, item = copy, fromSlot = i, toSlot = placeholderSlot, note = false, unnote = true)

            if (transfer != null) {
                deposited += transfer.completed
                if(placeholderSlot==-1 && curTab!=0 && oldFree!=to.freeSlotCount){ // shift newly add items to tab position
                    var fromPos = to.getItemIndex(copy.id, false)
                    fromPos = if(fromPos != -1) fromPos else to.nextFreeSlot-1
                    val toPos = insertionPoint(p, curTab)
                    to.insert(fromPos, toPos)
                    p.setVarbit(BANK_TAB_ROOT_VARBIT+curTab, p.getVarbit(BANK_TAB_ROOT_VARBIT+curTab)+1)
                }
            }
        }

        if (deposited == 0) {
            p.message("Bank full.")
        }
    }

    fun open(p: Player) {
        p.setInterfaceUnderlay(-1, -2)
        p.openInterface(BANK_INTERFACE_ID, InterfaceDestination.MAIN_SCREEN)
        p.openInterface(INV_INTERFACE_ID, InterfaceDestination.TAB_AREA)

        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 8, text = p.bank.capacity.toString())

        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 12, range = 0..815, setting = 1312766)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 12, range = 825..833, setting = 2)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 12, range = 834..843, setting = 1048576)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 10, range = 10..10, setting = 1048706)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 10, range = 11..19, setting = 1179842)
        p.setInterfaceEvents(interfaceId = INV_INTERFACE_ID, component = 3, range = 0..27, setting = 1181694)
        p.setInterfaceEvents(interfaceId = INV_INTERFACE_ID, component = 13, range = 0..27, setting = 1054)
        p.setInterfaceEvents(interfaceId = INV_INTERFACE_ID, component = 19, range = 0..27, setting = 1054)
        p.setInterfaceEvents(interfaceId = INV_INTERFACE_ID, component = 4, range = 0..27, setting = 1180674)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 46, range = 1..816, setting = 2)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 49, range = 0..3, setting = 2)

        p.setVarbit(BANK_YOUR_LOOT_VARBIT, 0)
    }

    fun ItemContainer.removePlaceholder(world: World, item: Item): Int {
        val def = item.toUnnoted(world.definitions).getDef(world.definitions)
        val slot = if (def.placeholderLink > 0) indexOfFirst { it?.id == def.placeholderLink && it.amount == 0 } else -1
        if (slot != -1) {
            this[slot] = null
        }
        return slot
    }

    fun ItemContainer.insert(from: Int, to: Int) {
        val fromItem = this[from]!! // Shouldn't be null

        this[from] = null

        if (from < to) {
            for (i in from until to) {
                this[i] = this[i + 1]
            }
        } else {
            for (i in from downTo to + 1) {
                this[i] = this[i - 1]
            }
        }
        this[to] = fromItem
    }
}