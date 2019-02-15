package gg.rsmod.plugins.osrs.content.skills.slayer

import gg.rsmod.plugins.osrs.api.cfg.Npcs
import gg.rsmod.plugins.osrs.api.ext.npcDialog

on_npc_option(npc = Npcs.KRYSTILIA, option = "talk-to", lineOfSightDistance = 1) {
    it.suspendable {
        it.npcDialog("Yeah? What do you want?")
    }
}