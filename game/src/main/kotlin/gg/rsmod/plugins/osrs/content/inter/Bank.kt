package gg.rsmod.plugins.osrs.content.inter

import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Bank {

    private const val BANK_INTERFACE_ID = 12
    private const val INV_INTERFACE_ID = 15
    private const val INV_INTERFAC_CHILD = 3

    private const val QUANTITY_VARBIT = 6590

    // withdraw as: 155
    // rearrange mode: 304
    // always placehold: 1052

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        intArrayOf(28, 30, 32, 34, 36).forEach { quantity ->
            r.bindButton(parent = BANK_INTERFACE_ID, child = quantity) {
                val state = (quantity - 28) / 2
                it.player().setVarbit(QUANTITY_VARBIT, state)
            }
        }
        r.bindButton(parent = INV_INTERFACE_ID, child = INV_INTERFAC_CHILD) {
            val p = it.player()

            val opt = it.getInteractingOption()
            val slot = it.getInteractingSlot()
            val item = p.inventory[slot] ?: return@bindButton

            if (opt == 9) {
                // TODO(Tom): examine message
                return@bindButton
            }

            val quantityVarbit = p.getVarbit(QUANTITY_VARBIT)
            var amount: Int

            if (quantityVarbit != 0) {
                if (opt == 1) {
                    amount = when (quantityVarbit) {
                        1 -> 5
                        2 -> 10
                        3 -> -1 // X
                        4 -> 0 // All
                        else -> return@bindButton
                    }
                } else {
                    amount = when (opt) {
                        2 -> 1
                        3 -> 5
                        4 -> 10
                        6 -> -1 // X
                        else -> return@bindButton
                    }
                }
            } else {
                amount = when (opt) {
                    1 -> 1
                    3 -> 5
                    4 -> 10
                    6 -> -1 // X
                    7 -> 0 // All
                    else -> return@bindButton
                }
            }
            if (amount == 0) {
                amount = p.inventory.getItemCount(item.id)
            } else if (amount == -1) {
                // Prompt for X and swap in suspendable block
                return@bindButton
            }

            var deposited = 0
            for (i in 0 until p.inventory.capacity) {
                val invItem = p.inventory[i] ?: continue
                if (invItem.id == item.id) {
                    if (invItem.amount + deposited <= amount) {
                        deposited += p.inventory.swap(p.bank, invItem, beginSlot = i)
                    } else {
                        deposited += p.inventory.swap(p.bank, invItem.id, amount - deposited, beginSlot = i)
                    }
                }
            }

            if (deposited == 0) {
                p.message("Bank full.")
            }
        }
    }
}