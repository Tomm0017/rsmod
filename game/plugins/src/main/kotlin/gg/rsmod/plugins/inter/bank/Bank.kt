package gg.rsmod.plugins.content.inter.bank

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.osrs.api.ext.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Bank {

    const val BANK_INTERFACE_ID = 12
    const val INV_INTERFACE_ID = 15
    const val INV_INTERFACE_CHILD = 3

    const val WITHDRAW_AS_VARBIT = 3958
    const val REARRANGE_MODE_VARBIT = 3959
    const val ALWAYS_PLACEHOLD_VARBIT = 3755
    const val LAST_X_INPUT = 3960
    const val SELECTED_TAB = 4150
    const val QUANTITY_VARBIT = 6590

    fun withdraw(p: Player, id: Int, amt: Int, slot: Int, placehold: Boolean) {
        var withdrawn = 0

        val from = p.bank
        val to = p.inventory

        val amount = Math.min(from.getItemCount(id), amt)

        for (i in slot until from.capacity) {
            val item = from[i] ?: continue
            if (item.id != id) {
                continue
            }

            if (withdrawn >= amount) {
                break
            }

            val left = amount - withdrawn
            withdrawn += from.swap(to, item.id, Math.min(left, item.amount), beginSlot = i, note = p.getVarbit(WITHDRAW_AS_VARBIT) == 1)

            if (from[i] == null) {
                if (placehold || p.getVarbit(ALWAYS_PLACEHOLD_VARBIT) == 1) {
                    val def = item.getDef(p.world.definitions)

                    /**
                     * Make sure the item has a valid placeholder item in its
                     * definition.
                     */
                    if (def.placeholderId > 0) {
                        p.bank.set(i, Item(def.placeholderId, 0))
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
        var deposited = 0

        val from = p.inventory
        val to = p.bank

        val amount = Math.min(from.getItemCount(id), amt)

        for (i in 0 until from.capacity) {
            val item = from[i] ?: continue
            if (item.id != id) {
                continue
            }

            if (deposited >= amount) {
                break
            }

            val left = amount - deposited
            deposited += from.swap(to, item.id, Math.min(left, item.amount), beginSlot = i, note = false)
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

        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 13, range = 0..815, setting = 1312766)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 13, range = 825..833, setting = 2)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 13, range = 834..843, setting = 1048576)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 11, range = 10..10, setting = 1048578)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 11, range = 11..19, setting = 1179714)
        p.setInterfaceEvents(interfaceId = INV_INTERFACE_ID, component = 3, range = 0..27, setting = 1181694)
        p.setInterfaceEvents(interfaceId = INV_INTERFACE_ID, component = 10, range = 0..27, setting = 1054)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 47, range = 1..816, setting = 2)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 50, range = 0..3, setting = 2)
    }
}