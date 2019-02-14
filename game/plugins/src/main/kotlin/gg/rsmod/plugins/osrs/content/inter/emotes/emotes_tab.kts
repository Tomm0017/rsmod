package gg.rsmod.plugins.osrs.content.inter.emotes

import gg.rsmod.plugins.osrs.api.ext.getInteractingSlot
import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.api.ext.setInterfaceEvents

on_login {
    it.player().setInterfaceEvents(parent = EmotesTab.COMPONENT_ID, child = 1, range = 0..47, setting = 2)
}

on_button(parent = EmotesTab.COMPONENT_ID, child = 1) p@ {
    val slot = it.getInteractingSlot()
    val emote = Emote.values.firstOrNull { e -> e.slot == slot } ?: return@p
    EmotesTab.performEmote(it.player(), emote)
}