package gg.rsmod.plugins.osrs

import gg.rsmod.game.message.impl.SendLogoutMessage
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
object OSRSGameframe {

    const val RUN_ENABLED_VARP = 173
    const val XP_DROPS_VISIBLE_VARBIT = 4702
    const val DATA_ORBS_HIDDEN_VARBIT = 4084

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        /**
         * Logout button.
         */
        r.bindButton(182, 8) {
            it.player().requestLogout()
            it.player().write(SendLogoutMessage())
            it.player().channelClose()
        }

        /**
         * Run button.
         */
        r.bindButton(160, 22) {
            if (it.player().runEnergy >= 1.0) {
                it.player().toggleVarp(RUN_ENABLED_VARP)
            } else {
                it.player().setVarp(RUN_ENABLED_VARP, 0)
                it.player().message("You don't have enough run energy left.")
            }
        }

        /**
         * XP Drop buttons.
         */
        r.bindButton(160, 1) {
            it.player().toggleVarbit(XP_DROPS_VISIBLE_VARBIT)
            if (it.player().getVarbit(XP_DROPS_VISIBLE_VARBIT) == 1) {
                it.player().openInterface(122, InterfacePane.XP_COUNTER)
            } else {
                it.player().closeInterface(122)
            }
        }

        /**
         * Settings.
         */
        r.bindButton(60, 20) {
            it.player().toggleVarbit(DATA_ORBS_HIDDEN_VARBIT)
            if (it.player().getVarbit(XP_DROPS_VISIBLE_VARBIT) == 1) {
                it.player().openInterface(160, InterfacePane.MINI_MAP)
            } else {
                it.player().closeInterface(160)
            }
        }
    }
}