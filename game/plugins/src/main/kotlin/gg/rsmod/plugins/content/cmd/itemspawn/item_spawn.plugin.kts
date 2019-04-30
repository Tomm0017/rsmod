package gg.rsmod.plugins.content.cmd.itemspawn

import gg.rsmod.game.model.priv.Privilege

on_command("spawn", Privilege.ADMIN_POWER) {
    player.queue(TaskPriority.STRONG) {
        val item = spawn() ?: return@queue
        if (item.amount > 0) {
            player.message("You have spawned ${item.amount} x ${item.getName(world.definitions)}.")
        } else {
            player.message("You don't have enough inventory space.")
        }
    }
}

suspend fun QueueTask.spawn(): Item? {
    val item = searchItemInput("Select an item to spawn:")
    if (item == -1) {
        return null
    }
    val amount = when (options("1", "5", "X", "Max", title = "How many would you like to spawn?")) {
        1 -> 1
        2 -> 5
        3 -> inputInt("Enter amount to spawn")
        4 -> Int.MAX_VALUE
        else -> return null
    }
    val add = player.inventory.add(item, amount, assureFullInsertion = false)
    return Item(item, add.completed)
}