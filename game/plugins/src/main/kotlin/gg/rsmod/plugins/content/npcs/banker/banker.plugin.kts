package gg.rsmod.plugins.content.npcs.banker

import gg.rsmod.plugins.content.inter.bank.openBank

arrayOf(Npcs.BANKER_1027, Npcs.BANKER_1028).forEach { banker ->
    on_npc_option(npc = banker, option = "talk-to", lineOfSightDistance = 2) {
        player.queue {
            dialog(this)
        }
    }
    on_npc_option(npc = banker, option = "bank", lineOfSightDistance = 2) {
        player.openBank()
    }
    on_npc_option(npc = banker, option = "collect", lineOfSightDistance = 2) {
        open_collect(player)
    }
}

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Good day, how may I help you?")
    when (options(it)) {
        1 -> it.player.openBank()
        2 -> open_pin(it.player)
        3 -> open_collect(it.player)
        4 -> what_is_this_place(it)
    }
}

suspend fun options(it: QueueTask): Int = it.options("I'd like to access my bank account, please.", "I'd like to check my PIN settings.", "I'd like to collect items.", "What is this place?")

suspend fun what_is_this_place(it: QueueTask) {
    it.chatNpc("This is a branch of the Bank of Gielinor. We have<br>branches in many towns.", animation = 568)
    it.chatPlayer("And what do you do?", animation = 554)
    it.chatNpc("We will look after your items and money for you.<br>Leave your valuables with us if you want to keep them<br>safe.", animation = 569)
}

fun open_collect(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 402, dest = InterfaceDestination.MAIN_SCREEN)
}

fun open_pin(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 14, dest = InterfaceDestination.MAIN_SCREEN)
}