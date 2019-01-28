
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.bank.Bank

onComponentClose(Bank.BANK_COMPONENT_ID) {
    it.player().closeComponent(Bank.INV_COMPONENT_ID)
}

intArrayOf(17, 19).forEachIndexed { index, button ->
    onButton(parent = Bank.BANK_COMPONENT_ID, child = button) {
        it.player().setVarbit(Bank.REARRANGE_MODE_VARBIT, index)
    }
}

intArrayOf(22, 24).forEachIndexed { index, button ->
    onButton(parent = Bank.BANK_COMPONENT_ID, child = button) {
        it.player().setVarbit(Bank.WITHDRAW_AS_VARBIT, index)
    }
}

onButton(parent = Bank.BANK_COMPONENT_ID, child = 38) {
    it.player().toggleVarbit(Bank.ALWAYS_PLACEHOLD_VARBIT)
}

intArrayOf(28, 30, 32, 34, 36).forEach { quantity ->
    onButton(parent = Bank.BANK_COMPONENT_ID, child = quantity) {
        val state = (quantity - 28) / 2
        it.player().setVarbit(Bank.QUANTITY_VARBIT, state)
    }
}

onButton(parent = Bank.BANK_COMPONENT_ID, child = 42) {
    val p = it.player()
    val from = p.inventory
    val to = p.bank

    var any = false
    for (i in 0 until from.capacity) {
        val item = from[i] ?: continue

        val total = item.amount
        val deposited = from.swap(to, item, beginSlot = i, note = false)
        if (total != deposited) {
            // Was not able to deposit the whole stack of [item].
        }
        if (deposited > 0) {
            any = true
        }
    }

    if (!any && !from.isEmpty()) {
        p.message("Bank full.")
    }
}

onButton(parent = Bank.BANK_COMPONENT_ID, child = 44) {
    val p = it.player()
    val from = p.equipment
    val to = p.bank

    var any = false
    for (i in 0 until from.capacity) {
        val item = from[i] ?: continue

        val total = item.amount
        val deposited = from.swap(to, item, beginSlot = i, note = false)
        if (total != deposited) {
            // Was not able to deposit the whole stack of [item].
        }
        if (deposited > 0) {
            any = true
        }
    }

    if (!any && !from.isEmpty()) {
        p.message("Bank full.")
    }
}

onButton(parent = Bank.INV_COMPONENT_ID, child = Bank.INV_COMPONENT_CHILD) p@ {
    val p = it.player()

    val opt = it.getInteractingOption()
    val slot = it.getInteractingSlot()
    val item = p.inventory[slot] ?: return@p

    if (opt == 9) {
        p.world.sendExamine(p, item.id, ExamineEntityType.ITEM)
        return@p
    }

    val quantityVarbit = p.getVarbit(Bank.QUANTITY_VARBIT)
    var amount: Int

    when {
        quantityVarbit == 0 -> amount = when (opt) {
            1 -> 1
            3 -> 5
            4 -> 10
            5 -> p.getVarbit(Bank.LAST_X_INPUT)
            6 -> -1 // X
            7 -> 0 // All
            else -> return@p
        }
        opt == 1 -> amount = when (quantityVarbit) {
            1 -> 5
            2 -> 10
            3 -> if (p.getVarbit(Bank.LAST_X_INPUT) == 0) -1 else p.getVarbit(Bank.LAST_X_INPUT)
            4 -> 0 // All
            else -> return@p
        }
        else -> amount = when (opt) {
            2 -> 1
            3 -> 5
            4 -> 10
            5 -> p.getVarbit(Bank.LAST_X_INPUT)
            6 -> -1 // X
            7 -> 0 // All
            else -> return@p
        }
    }

    if (amount == 0) {
        amount = p.inventory.getItemCount(item.id)
    } else if (amount == -1) {
        it.suspendable {
            amount = it.inputInteger("How many would you like to bank?")
            if (amount > 0) {
                p.setVarbit(Bank.LAST_X_INPUT, amount)
                Bank.deposit(p, item.id, amount)
            }
        }
        return@p
    }

    Bank.deposit(p, item.id, amount)
}

onButton(parent = Bank.BANK_COMPONENT_ID, child = 13) p@ {
    val p = it.player()

    val opt = it.getInteractingOption()
    val slot = it.getInteractingSlot()
    val item = p.bank[slot] ?: return@p

    if (opt == 9) {
        p.world.sendExamine(p, item.id, ExamineEntityType.ITEM)
        return@p
    }

    var amount: Int
    var placehold = false

    val quantityVarbit = p.getVarbit(Bank.QUANTITY_VARBIT)
    when {
        quantityVarbit == 0 -> amount = when (opt) {
            0 -> 1
            2 -> 5
            3 -> 10
            4 -> p.getVarbit(Bank.LAST_X_INPUT)
            5 -> -1 // X
            6 -> item.amount
            7 -> item.amount - 1
            8 -> {
                placehold = true
                item.amount
            }
            else -> return@p
        }
        opt == 0 -> amount = when (quantityVarbit) {
            0 -> 1
            1 -> 5
            2 -> 10
            3 -> if (p.getVarbit(Bank.LAST_X_INPUT) == 0) -1 else p.getVarbit(Bank.LAST_X_INPUT)
            4 -> item.amount
            8 -> {
                placehold = true
                item.amount
            }
            else -> return@p
        }
        else -> amount = when (opt) {
            1 -> 1
            2 -> 5
            3 -> 10
            4 -> p.getVarbit(Bank.LAST_X_INPUT)
            5 -> -1 // X
            6 -> item.amount
            7 -> item.amount - 1
            8 -> {
                placehold = true
                item.amount
            }
            else -> return@p
        }
    }

    if (amount == -1) {
        /**
         * Placeholders' "release" option uses the same option
         * as "withdraw-x" would.
         */
        if (item.amount == 0) {
            p.bank.set(slot, null)
            return@p
        }
        it.suspendable {
            amount = it.inputInteger("How many would you like to withdraw?")
            if (amount > 0) {
                p.setVarbit(Bank.LAST_X_INPUT, amount)
                Bank.withdraw(p, item.id, amount, slot, placehold)
            }
        }
        return@p
    }

    amount = Math.max(0, amount)
    if (amount > 0) {
        Bank.withdraw(p, item.id, amount, slot, placehold)
    }
}