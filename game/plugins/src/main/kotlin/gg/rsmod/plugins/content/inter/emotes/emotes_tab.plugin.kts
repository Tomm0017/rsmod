package gg.rsmod.plugins.content.inter.emotes

import gg.rsmod.plugins.content.inter.emotes.EmotesTab.COMPONENT_ID
import gg.rsmod.plugins.content.inter.emotes.EmotesTab.performEmote

on_login {
    player.setInterfaceEvents(interfaceId = COMPONENT_ID, component = 1, range = 0..47, setting = 2)
}

on_button(interfaceId = COMPONENT_ID, component = 1) p@ {
    val slot = player.getInteractingSlot()
    val emote = Emote.values.firstOrNull { e -> e.slot == slot } ?: return@p
    performEmote(player, emote)
}