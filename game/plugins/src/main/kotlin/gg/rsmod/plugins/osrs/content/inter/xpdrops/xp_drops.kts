import gg.rsmod.plugins.osrs.api.InterfacePane
import gg.rsmod.plugins.osrs.api.OSRSGameframe
import gg.rsmod.plugins.osrs.api.helper.*

/**
 * XP Drop buttons.
 */
r.bindButton(160, 1) {
    it.player().toggleVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT)
    if (it.player().getVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT) == 1) {
        it.player().openInterface(interfaceId = 122, pane =  InterfacePane.XP_COUNTER)
    } else {
        it.player().closeInterface(interfaceId = 122)
    }
}

/**
 * TODO(Tom): move this to the inner settings plugin when it's created.
 */
/*
r.bindButton(60, 20) {
    it.player().toggleVarbit(OSRSGameframe.DATA_ORBS_HIDDEN_VARBIT)
    if (it.player().getVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT) == 1) {
        it.player().openInterface(interfaceId = 160, pane = InterfacePane.MINI_MAP)
    } else {
        it.player().closeInterface(interfaceId = 160)
    }
}*/
