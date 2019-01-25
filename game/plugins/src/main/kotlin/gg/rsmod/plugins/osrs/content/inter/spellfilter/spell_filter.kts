
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.plugins.osrs.api.getDisplayInterfaceId
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.spellfilter.SpellFilters

onLogin {
    it.player().setInterfaceSetting(parent = SpellFilters.INTERFACE_ID, child = 184, range = 0..4, setting = 2)
}

DisplayMode.values().forEach { mode ->
    val child = when (mode) {
        DisplayMode.RESIZABLE_NORMAL -> 57
        DisplayMode.RESIZABLE_LIST -> 56
        DisplayMode.FIXED -> 54
        else -> return@forEach
    }
    onButton(parent = getDisplayInterfaceId(mode), child = child) {
        val opt = it.getInteractingOption()
        if (opt == 1) {
            it.player().toggleVarbit(SpellFilters.DISABLE_FILTERS_VARBIT)
        }
    }
}

onButton(parent = SpellFilters.INTERFACE_ID, child = 184) {
    val slot = it.getInteractingSlot()
    val varbit = when (slot) {
        0 -> SpellFilters.FILTER_COMBAT_VARBIT
        1 -> SpellFilters.FILTER_TELEPORTS_VARBIT
        2 -> SpellFilters.FILTER_UTILITY_VARBIT
        3 -> SpellFilters.FILTER_BY_LEVEL_VARBIT
        4 -> SpellFilters.FILTER_BY_RUNES_VARBIT
        else -> return@onButton
    }
    it.player().toggleVarbit(varbit)
}