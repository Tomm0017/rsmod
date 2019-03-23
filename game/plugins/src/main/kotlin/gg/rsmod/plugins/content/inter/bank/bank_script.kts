package gg.rsmod.plugins.content.inter.bank

on_interface_close(Bank.BANK_INTERFACE_ID) {
    player.closeInterface(dest = InterfaceDestination.TAB_AREA)
}

intArrayOf(17, 19).forEachIndexed { index, button ->
    on_button(interfaceId = Bank.BANK_INTERFACE_ID, component = button) {
        player.setVarbit(Bank.REARRANGE_MODE_VARBIT, index)
    }
}

intArrayOf(22, 24).forEachIndexed { index, button ->
    on_button(interfaceId = Bank.BANK_INTERFACE_ID, component = button) {
        player.setVarbit(Bank.WITHDRAW_AS_VARBIT, index)
    }
}

on_button(interfaceId = Bank.BANK_INTERFACE_ID, component = 38) {
    player.toggleVarbit(Bank.ALWAYS_PLACEHOLD_VARBIT)
}

intArrayOf(28, 30, 32, 34, 36).forEach { quantity ->
    on_button(interfaceId = Bank.BANK_INTERFACE_ID, component = quantity) {
        val state = (quantity - 28) / 2
        player.setVarbit(Bank.QUANTITY_VARBIT, state)
    }
}

on_button(interfaceId = Bank.BANK_INTERFACE_ID, component = 42) {
    val p = player
    val from = p.inventory
    val to = p.bank

    var any = false
    for (i in 0 until from.capacity) {
        val item = from[i] ?: continue

        val total = item.amount
        val deposited = from.transfer(to, item, beginSlot = i, note = false, unnote = true)
        if (total != deposited) {
            // Was not able to deposit the whole stack of [item].
        }
        if (deposited > 0) {
            any = true
        }
    }

    if (!any && !from.isEmpty) {
        p.message("Bank full.")
    }
}

on_button(interfaceId = Bank.BANK_INTERFACE_ID, component = 44) {
    val p = player
    val from = p.equipment
    val to = p.bank

    var any = false
    for (i in 0 until from.capacity) {
        val item = from[i] ?: continue

        val total = item.amount
        val deposited = from.transfer(to, item, beginSlot = i, note = false, unnote = true)
        if (total != deposited) {
            // Was not able to deposit the whole stack of [item].
        }
        if (deposited > 0) {
            any = true
        }
    }

    if (!any && !from.isEmpty) {
        p.message("Bank full.")
    }
}

on_button(interfaceId = Bank.INV_INTERFACE_ID, component = Bank.INV_INTERFACE_CHILD) p@ {
    val opt = player.getInteractingOption()
    val slot = player.getInteractingSlot()
    val item = player.inventory[slot] ?: return@p

    if (opt == 10) {
        player.world.sendExamine(player, item.id, ExamineEntityType.ITEM)
        return@p
    }

    val quantityVarbit = player.getVarbit(Bank.QUANTITY_VARBIT)
    var amount: Int

    when {
        quantityVarbit == 0 -> amount = when (opt) {
            2 -> 1
            4 -> 5
            5 -> 10
            6 -> player.getVarbit(Bank.LAST_X_INPUT)
            7 -> -1 // X
            8 -> 0 // All
            else -> return@p
        }
        opt == 2 -> amount = when (quantityVarbit) {
            1 -> 5
            2 -> 10
            3 -> if (player.getVarbit(Bank.LAST_X_INPUT) == 0) -1 else player.getVarbit(Bank.LAST_X_INPUT)
            4 -> 0 // All
            else -> return@p
        }
        else -> amount = when (opt) {
            3 -> 1
            4 -> 5
            5 -> 10
            6 -> player.getVarbit(Bank.LAST_X_INPUT)
            7 -> -1 // X
            8 -> 0 // All
            else -> return@p
        }
    }

    if (amount == 0) {
        amount = player.inventory.getItemCount(item.id)
    } else if (amount == -1) {
        this.player.queue {
            amount = inputInt("How many would you like to bank?")
            if (amount > 0) {
                player.setVarbit(Bank.LAST_X_INPUT, amount)
                Bank.deposit(player, item.id, amount)
            }
        }
        return@p
    }

    Bank.deposit(player, item.id, amount)
}

on_button(interfaceId = Bank.BANK_INTERFACE_ID, component = 13) p@ {
    val opt = player.getInteractingOption()
    val slot = player.getInteractingSlot()
    val item = player.bank[slot] ?: return@p

    if (opt == 10) {
        player.world.sendExamine(player, item.id, ExamineEntityType.ITEM)
        return@p
    }

    var amount: Int
    var placehold = false

    val quantityVarbit = player.getVarbit(Bank.QUANTITY_VARBIT)
    when {
        quantityVarbit == 0 -> amount = when (opt) {
            1 -> 1
            3 -> 5
            4 -> 10
            5 -> player.getVarbit(Bank.LAST_X_INPUT)
            6 -> -1 // X
            7 -> item.amount
            8 -> item.amount - 1
            9 -> {
                placehold = true
                item.amount
            }
            else -> return@p
        }
        opt == 1 -> amount = when (quantityVarbit) {
            0 -> 1
            1 -> 5
            2 -> 10
            3 -> if (player.getVarbit(Bank.LAST_X_INPUT) == 0) -1 else player.getVarbit(Bank.LAST_X_INPUT)
            4 -> item.amount
            8 -> {
                placehold = true
                item.amount
            }
            else -> return@p
        }
        else -> amount = when (opt) {
            2 -> 1
            3 -> 5
            4 -> 10
            5 -> player.getVarbit(Bank.LAST_X_INPUT)
            6 -> -1 // X
            7 -> item.amount
            8 -> item.amount - 1
            9 -> {
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
            player.bank.set(slot, null)
            return@p
        }
        this.player.queue {
            amount = inputInt("How many would you like to withdraw?")
            if (amount > 0) {
                player.setVarbit(Bank.LAST_X_INPUT, amount)
                Bank.withdraw(player, item.id, amount, slot, placehold)
            }
        }
        return@p
    }

    amount = Math.max(0, amount)
    if (amount > 0) {
        Bank.withdraw(player, item.id, amount, slot, placehold)
    }
}