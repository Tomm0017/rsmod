package gg.rsmod.plugins.content.areas.motherlode.objs

import gg.rsmod.plugins.content.areas.motherlode.WaterCircuit
import gg.rsmod.plugins.content.areas.motherlode.WaterCircuit.SACK_COUNT_VARBIT

val EMPTY_SACK = 26677
val SACK = 26678

on_obj_option(EMPTY_SACK, 1) {
    player.message("the sack is empty")
}

on_obj_option(SACK, 1) {
    val pending = player.attr.getOrDefault(WaterCircuit.PENDING_ORES_ATTR, ArrayList())
    player.queue {
        for(dex in pending.size-1 downTo 0){
            if(player.getVarbit(SACK_COUNT_VARBIT) == 0){
                player.message("your sack is empty and ${pending.size} ores still waiting!")
                break
            }
            if(player.inventory.add(pending[dex]).hasSucceeded()){
                pending.removeAt(dex)
                player.decrementVarbit(SACK_COUNT_VARBIT)
                player.message("sack size: ${player.getVarbit(SACK_COUNT_VARBIT)} -> ores left: ${pending.size}")
            } else
                player.message("You inventory is too full to carry another!")
        }
    }
}