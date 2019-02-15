package gg.rsmod.plugins.osrs.content.npcs

import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.cfg.Npcs
import gg.rsmod.plugins.osrs.api.ext.npcDialog
import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.content.inter.bank.Bank

arrayOf(Npcs.BANKER, Npcs.BANKER_395).forEach { banker ->
    on_npc_option(npc = banker, option = "talk-to", lineOfSightDistance = 2) {
        it.suspendable { dialog(it) }
    }
    on_npc_option(npc = banker, option = "bank", lineOfSightDistance = 2) {
        Bank.open(it.player())
    }
}

suspend fun dialog(it: Plugin) {
    it.npcDialog("Good day, how may I help you?")
}