package gg.rsmod.plugins.content.areas.edgeville.chat

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Npcs
import gg.rsmod.plugins.api.ext.*

on_npc_option(npc = Npcs.ABBOT_LANGLEY, option = "talk-to") {
    it.suspendable { dialog(it) }
}

suspend fun dialog(it: Plugin) {
    it.chatNpc("Greetings traveller.", animation = 588)
    when (it.options("Can you heal me? I'm injured.", "Isn't this place built a bit out of the way?")) {
        1 -> {
            val player = it.player()
            it.chatPlayer("Can you heal me? I'm injured.", animation = 554)
            it.chatNpc("Ok.", animation = 588)
            heal(player)
            it.messageBox("Abbot Langley places his hands on your head. You feel a little better.")
        }
        2 -> {
            it.chatPlayer("Isn't this place built a bit out of the way?", animation = 554)
            it.chatNpc("We like it that way actually! We get disturbed less. We<br>still get rather a large amount of travellers looking for<br>sanctuary and healing here as it is!", animation = 590)
        }
    }
}

fun heal(p: Player) {
    if (p.getSkills().getCurrentLevel(Skills.HITPOINTS) < p.getSkills().getMaxLevel(Skills.HITPOINTS)) {
        p.getSkills().setCurrentLevel(Skills.HITPOINTS, p.getSkills().getMaxLevel(Skills.HITPOINTS))
    }
}