
import gg.rsmod.plugins.osrs.api.helper.getInteractingSlot
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.setInterfaceSetting
import gg.rsmod.plugins.osrs.content.inter.emotes.Emote
import gg.rsmod.plugins.osrs.content.inter.emotes.EmotesTab

onLogin {
    it.player().setInterfaceSetting(parent = EmotesTab.INTERFACE_ID, child = 1, range = 0..47, setting = 2)
}

onButton(parent = EmotesTab.INTERFACE_ID, child = 1) p@ {
    val slot = it.getInteractingSlot()
    val emote = Emote.values().firstOrNull { e -> e.slot == slot } ?: return@p
    EmotesTab.performEmote(it.player(), emote)
}