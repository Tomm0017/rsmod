package gg.rsmod.plugins.content.inter.spellfilter

import gg.rsmod.plugins.content.inter.spellfilter.SpellFilters.SPELL_FILTER_INTERFACE_ID
import gg.rsmod.plugins.content.inter.spellfilter.SpellFilters.SPELL_FILTER_COMPONENT_ID
import gg.rsmod.plugins.content.inter.spellfilter.SpellFilters.DISABLE_FILTERS_VARBIT
import gg.rsmod.plugins.content.inter.spellfilter.SpellFilters.FILTER_COMBAT_VARBIT
import gg.rsmod.plugins.content.inter.spellfilter.SpellFilters.FILTER_BY_LEVEL_VARBIT
import gg.rsmod.plugins.content.inter.spellfilter.SpellFilters.FILTER_BY_RUNES_VARBIT
import gg.rsmod.plugins.content.inter.spellfilter.SpellFilters.FILTER_TELEPORTS_VARBIT
import gg.rsmod.plugins.content.inter.spellfilter.SpellFilters.FILTER_UTILITY_VARBIT
import gg.rsmod.game.model.interf.DisplayMode

on_login {
    player.setInterfaceEvents(interfaceId = SPELL_FILTER_INTERFACE_ID, component = SPELL_FILTER_COMPONENT_ID, range = 0..4, setting = 2)
}

DisplayMode.values.forEach { mode ->
    val child = when (mode) {
        DisplayMode.RESIZABLE_NORMAL -> 77
        DisplayMode.RESIZABLE_LIST -> 77
        DisplayMode.FIXED -> 75
        else -> return@forEach
    }
    on_button(interfaceId = getDisplayComponentId(mode), component = child) {
        val opt = player.getInteractingOption()
        if (opt == 2) {
            player.toggleVarbit(DISABLE_FILTERS_VARBIT)
        }
    }
}

on_button(interfaceId = SPELL_FILTER_INTERFACE_ID, component = SPELL_FILTER_COMPONENT_ID) {
    val varbit = when (player.getInteractingSlot()) {
        0 -> FILTER_COMBAT_VARBIT
        1 -> FILTER_TELEPORTS_VARBIT
        2 -> FILTER_UTILITY_VARBIT
        3 -> FILTER_BY_LEVEL_VARBIT
        4 -> FILTER_BY_RUNES_VARBIT
        else -> return@on_button
    }
    player.toggleVarbit(varbit)
}