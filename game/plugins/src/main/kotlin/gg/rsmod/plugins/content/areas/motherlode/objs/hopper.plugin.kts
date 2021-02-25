package gg.rsmod.plugins.content.areas.motherlode.objs

import gg.rsmod.plugins.content.areas.motherlode.WaterCircuit.float
import gg.rsmod.plugins.content.areas.motherlode.WaterCircuit.SACK_COUNT_VARBIT
import gg.rsmod.plugins.content.areas.motherlode.WaterCircuit.PENDING_ORES_ATTR
import gg.rsmod.plugins.content.areas.motherlode.WaterCircuit.WHEELS_RUNNING
import kotlin.random.Random

val SPAWN_TILE = Tile(3748, 5672)
val END_TILE = Tile(3748, 5660)

val ORES = arrayOf(Items.COAL, Items.GOLDEN_NUGGET, Items.GOLD_ORE, Items.MITHRIL_ORE, Items.ADAMANTITE_ORE, Items.RUNITE_ORE)

on_enter_region(14936) {
    player.openInterface(382, InterfaceDestination.OVERLAY)
}

on_exit_region(14936) {
    player.closeInterface(382)
}

on_obj_option(Objs.HOPPER_26674, "Deposit", 2) {
    if(player.getVarbit(SACK_COUNT_VARBIT) == 81){
        player.message("your sack is too full!")
        return@on_obj_option
    }

    if(WHEELS_RUNNING < 1){
        player.message("you need to get the wheels moving")
        return@on_obj_option
    }

    val amount = player.inventory.remove(Items.PAYDIRT, 28).completed

    val pending = player.attr.getOrDefault(PENDING_ORES_ATTR, ArrayList())
    if(pending.size == 0){
        for(i in 1..amount)
            pending.add(ORES[Random.nextInt(6)])
        player.attr[PENDING_ORES_ATTR] = pending
    }

    player.message("cleaning $amount paydirt...")

    if(amount <= 0)
        player.message("You do not have any pay-dirt to clean!")
    else{
        val payload = Npc(Npcs.PAYDIRT_6564, SPAWN_TILE, world)
        float(world, player, payload, amount)
    }
}