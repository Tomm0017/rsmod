package gg.rsmod.plugins.content.areas.edgeville.npcs

import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.api.cfg.Npcs
import gg.rsmod.plugins.api.ext.chatNpc
import gg.rsmod.plugins.api.ext.chatPlayer

on_npc_option(npc = Npcs.BROTHER_ALTHRIC, option = "talk-to") {
    it.suspendable { dialog(it) }
}

suspend fun dialog(it: Plugin) {
    it.chatPlayer("Very nice rosebushes you have here.", animation = 588)
    it.chatNpc("Yes, it has taken me many long hours in this garden to<br>bring them to this state of near-perfection.", animation = 589)
}