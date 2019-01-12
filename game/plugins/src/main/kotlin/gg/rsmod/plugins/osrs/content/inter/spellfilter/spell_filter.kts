import gg.rsmod.plugins.osrs.api.helper.getInteractingSlot
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.setInterfaceSetting
import gg.rsmod.plugins.osrs.api.helper.toggleVarbit
import gg.rsmod.plugins.osrs.content.inter.spellfilter.SpellFilters

r.bindLogin {
    it.player().setInterfaceSetting(parent = SpellFilters.INTERFACE_ID, child = 184, range = 0..4, setting = 2)
}

r.bindButton(parent = SpellFilters.INTERFACE_ID, child = 184) {
    val slot = it.getInteractingSlot()
    val varbit = when (slot) {
        0 -> SpellFilters.FILTER_COMBAT_VARBIT
        1 -> SpellFilters.FILTER_TELEPORTS_VARBIT
        2 -> SpellFilters.FILTER_UTILITY_VARBIT
        3 -> SpellFilters.FILTER_BY_LEVEL_VARBIT
        4 -> SpellFilters.FILTER_BY_RUNES_VARBIT
        else -> return@bindButton
    }
    it.player().toggleVarbit(varbit)
}