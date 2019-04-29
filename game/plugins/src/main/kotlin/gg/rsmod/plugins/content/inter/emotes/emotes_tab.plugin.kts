package gg.rsmod.plugins.content.inter.emotes

on_login {
    player.setInterfaceEvents(interfaceId = EmotesTab.COMPONENT_ID, component = 1, range = 0..47, setting = 2)
}

on_button(interfaceId = EmotesTab.COMPONENT_ID, component = 1) p@ {
    val slot = player.getInteractingSlot()
    val emote = Emote.values.firstOrNull { e -> e.slot == slot } ?: return@p
    EmotesTab.performEmote(player, emote)
}