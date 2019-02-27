package gg.rsmod.plugins.content.skills.slayer

import gg.rsmod.plugins.api.cfg.Npcs
import gg.rsmod.plugins.api.ext.chatNpc

on_npc_option(npc = Npcs.KRYSTILIA, option = "talk-to", lineOfSightDistance = 1) {
    suspendable {
        it.chatNpc("Yeah? What do you want?")
    }
}