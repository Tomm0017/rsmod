package gg.rsmod.plugins.content.inter.spellfilter

import gg.rsmod.game.model.interf.DisplayMode

on_login {
    player.setInterfaceEvents(interfaceId = SpellFilters.INTERFACE_ID, component = 184, range = 0..4, setting = 2)
}

DisplayMode.values.forEach { mode ->
    val child = when (mode) {
        DisplayMode.RESIZABLE_NORMAL -> 57
        DisplayMode.RESIZABLE_LIST -> 56
        DisplayMode.FIXED -> 54
        else -> return@forEach
    }
    on_button(interfaceId = getDisplayComponentId(mode), component = child) {
        val opt = player.getInteractingOption()
        if (opt == 2) {
            player.toggleVarbit(SpellFilters.DISABLE_FILTERS_VARBIT)
        }
    }
}

on_button(interfaceId = SpellFilters.INTERFACE_ID, component = 184) {
    val slot = player.getInteractingSlot()
    val varbit = when (slot) {
        0 -> SpellFilters.FILTER_COMBAT_VARBIT
        1 -> SpellFilters.FILTER_TELEPORTS_VARBIT
        2 -> SpellFilters.FILTER_UTILITY_VARBIT
        3 -> SpellFilters.FILTER_BY_LEVEL_VARBIT
        4 -> SpellFilters.FILTER_BY_RUNES_VARBIT
        else -> return@on_button
    }
    player.toggleVarbit(varbit)
}