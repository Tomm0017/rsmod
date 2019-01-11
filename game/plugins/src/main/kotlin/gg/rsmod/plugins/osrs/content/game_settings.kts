
import gg.rsmod.game.message.impl.SendLogoutMessage
import gg.rsmod.plugins.osrs.api.*
import gg.rsmod.plugins.osrs.api.helper.*

/**
 * @author Tom <rspsmods@gmail.com>
 */

/**
 * Logout button.
 */
r.bindButton(182, 8) {
    it.player().requestLogout()
    it.player().write(SendLogoutMessage())
    it.player().channelClose()
}

/**
 * XP Drop buttons.
 */
r.bindButton(160, 1) {
    it.player().toggleVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT)
    if (it.player().getVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT) == 1) {
        it.player().openInterface(122, InterfacePane.XP_COUNTER)
    } else {
        it.player().closeInterface(122)
    }
}

/**
 * Settings.
 */
r.bindButton(60, 20) {
    it.player().toggleVarbit(OSRSGameframe.DATA_ORBS_HIDDEN_VARBIT)
    if (it.player().getVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT) == 1) {
        it.player().openInterface(160, InterfacePane.MINI_MAP)
    } else {
        it.player().closeInterface(160)
    }
}