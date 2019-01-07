package gg.rsmod.plugins.osrs.content.inter

import gg.rsmod.game.model.Privilege
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.*
import gg.rsmod.plugins.osrs.InterfacePane

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Bank {

    private const val BANK_INTERFACE_ID = 12
    private const val INV_INTERFACE_ID = 15
    private const val INV_INTERFAC_CHILD = 3

    private const val WITHDRAW_AS_VARBIT = 3958
    private const val REARRANGE_MODE_VARBIT = 3959
    private const val ALWAYS_PLACEHOLD_VARBIT = 3755
    private const val LAST_X_INPUT = 3960
    private const val SELECTED_TAB = 4150
    private const val QUANTITY_VARBIT = 6590

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        r.bindCommand("obank", Privilege.ADMIN_POWER) {
            val p = it.player()
            open(p)
        }

        r.bindInterfaceClose(BANK_INTERFACE_ID) {
            it.player().closeInterface(INV_INTERFACE_ID)
        }

        r.bindButton(parent = BANK_INTERFACE_ID, child = 17) {
            it.player().setVarbit(REARRANGE_MODE_VARBIT, 0)
        }

        r.bindButton(parent = BANK_INTERFACE_ID, child = 19) {
            it.player().setVarbit(REARRANGE_MODE_VARBIT, 1)
        }

        r.bindButton(parent = BANK_INTERFACE_ID, child = 22) {
            it.player().setVarbit(WITHDRAW_AS_VARBIT, 0)
        }

        r.bindButton(parent = BANK_INTERFACE_ID, child = 24) {
            it.player().setVarbit(WITHDRAW_AS_VARBIT, 1)
        }

        r.bindButton(parent = BANK_INTERFACE_ID, child = 38) {
            it.player().toggleVarbit(ALWAYS_PLACEHOLD_VARBIT)
        }

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

            when {
                quantityVarbit == 0 -> amount = when (opt) {
                    1 -> 1
                    3 -> 5
                    4 -> 10
                    5 -> p.getVarbit(LAST_X_INPUT)
                    6 -> -1 // X
                    7 -> 0 // All
                    else -> return@bindButton
                }
                opt == 1 -> amount = when (quantityVarbit) {
                    1 -> 5
                    2 -> 10
                    3 -> if (p.getVarbit(LAST_X_INPUT) == 0) -1 else p.getVarbit(LAST_X_INPUT)
                    4 -> 0 // All
                    else -> return@bindButton
                }
                else -> amount = when (opt) {
                    2 -> 1
                    3 -> 5
                    4 -> 10
                    5 -> p.getVarbit(LAST_X_INPUT)
                    6 -> -1 // X
                    else -> return@bindButton
                }
            }

            if (amount == 0) {
                amount = p.inventory.getItemCount(item.id)
            } else if (amount == -1) {
                it.suspendable {
                    amount = it.inputInteger("How many would you like to bank?")
                    if (amount > 0) {
                        p.setVarbit(LAST_X_INPUT, amount)
                        deposit(p, item.id, amount)
                    }
                }
                return@bindButton
            }

            deposit(p, item.id, amount)
        }

        r.bindButton(parent = BANK_INTERFACE_ID, child = 13) {
            val p = it.player()

            val opt = it.getInteractingOption()
            val slot = it.getInteractingSlot()
            val item = p.bank[slot] ?: return@bindButton

            if (opt == 9) {
                // TODO(Tom): examine message
                return@bindButton
            }

            var amount: Int

            val quantityVarbit = p.getVarbit(QUANTITY_VARBIT)
            when {
                quantityVarbit == 0 -> amount = when (opt) {
                    0 -> 1
                    2 -> 5
                    3 -> 10
                    4 -> p.getVarbit(LAST_X_INPUT)
                    5 -> -1 // X
                    6 -> item.amount
                    7 -> item.amount - 1
                    else -> return@bindButton
                }
                opt == 0 -> amount = when (quantityVarbit) {
                    0 -> 1
                    1 -> 5
                    2 -> 10
                    3 -> if (p.getVarbit(LAST_X_INPUT) == 0) -1 else p.getVarbit(LAST_X_INPUT)
                    4 -> item.amount
                    else -> return@bindButton
                }
                else -> amount = when (opt) {
                    1 -> 1
                    2 -> 5
                    3 -> 10
                    4 -> p.getVarbit(LAST_X_INPUT)
                    5 -> -1 // X
                    6 -> item.amount
                    7 -> item.amount - 1
                    else -> return@bindButton
                }
            }

            if (amount == -1) {
                it.suspendable {
                    amount = it.inputInteger("How many would you like to withdraw?")
                    if (amount > 0) {
                        p.setVarbit(LAST_X_INPUT, amount)
                        withdraw(p, item.id, amount, slot)
                    }
                }
                return@bindButton
            }

            amount = Math.max(0, amount)
            if (amount > 0) {
                withdraw(p, item.id, amount, slot)
            }
        }
    }

    private fun open(p: Player) {
        p.setMainInterfaceBackground(-1, -2)
        p.openInterface(BANK_INTERFACE_ID, InterfacePane.MAIN_SCREEN)
        p.openInterface(INV_INTERFACE_ID, InterfacePane.TAB_AREA)

        p.setInterfaceText(parent = BANK_INTERFACE_ID, child = 8, text = p.bank.capacity.toString())

        p.setInterfaceSetting(parent = BANK_INTERFACE_ID, child = 13, range = 0..815, setting = 1312766)
        p.setInterfaceSetting(parent = BANK_INTERFACE_ID, child = 13, range = 825..833, setting = 2)
        p.setInterfaceSetting(parent = BANK_INTERFACE_ID, child = 13, range = 834..843, setting = 1048576)
        p.setInterfaceSetting(parent = BANK_INTERFACE_ID, child = 11, range = 10..10, setting = 1048578)
        p.setInterfaceSetting(parent = BANK_INTERFACE_ID, child = 11, range = 11..19, setting = 1179714)
        p.setInterfaceSetting(parent = INV_INTERFACE_ID, child = 3, range = 0..27, setting = 1181438)
        p.setInterfaceSetting(parent = INV_INTERFACE_ID, child = 10, range = 0..27, setting = 1054)
        p.setInterfaceSetting(parent = BANK_INTERFACE_ID, child = 47, range = 1..816, setting = 2)
        p.setInterfaceSetting(parent = BANK_INTERFACE_ID, child = 50, range = 0..3, setting = 2)
    }

    private fun withdraw(p: Player, id: Int, amount: Int, slot: Int) {
        var withdrawn = 0

        val from = p.bank
        val to = p.inventory

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
        }

        if (withdrawn == 0) {
            p.message("You don't have enough inventory space.")
        } else if (withdrawn != amount) {
            p.message("You don't have enough inventory space to withdraw that many.")
        }
    }

    private fun deposit(p: Player, id: Int, amount: Int) {
        var deposited = 0

        val from = p.inventory
        val to = p.bank

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
}