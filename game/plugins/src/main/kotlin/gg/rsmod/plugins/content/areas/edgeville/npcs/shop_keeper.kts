package gg.rsmod.plugins.content.areas.edgeville.npcs

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.api.cfg.Npcs
import gg.rsmod.plugins.api.ext.chatNpc
import gg.rsmod.plugins.api.ext.chatPlayer
import gg.rsmod.plugins.api.ext.options
import gg.rsmod.plugins.api.ext.player

on_npc_option(npc = Npcs.SHOP_KEEPER_514, option = "talk-to") {
    it.suspendable { dialog(it) }
}

suspend fun dialog(it: Plugin) {
    it.chatNpc("Can I help you at all?", animation = 567)
    when (it.options("Yes please. What are you selling?", "No thanks.")) {
        1 -> open_shop(it.player())
        2 -> it.chatPlayer("No thanks.", animation = 588)
    }
}

fun open_shop(p: Player) {

}