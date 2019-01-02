package gg.rsmod.plugins.osrs

import gg.rsmod.game.model.NEW_ACCOUNT_ATTR
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.*
import gg.rsmod.plugins.osrs.content.SpecialEnergy

/**
 * @author Tom <rspsmods@gmail.com>
 */
object OSRSLogin {

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        r.bindLogin {

            /**
             * First log-in logic (when accounts have just been made).
             */
            if (it.player().attr.getOrDefault(NEW_ACCOUNT_ATTR, false)) {
                SpecialEnergy.setEnergy(it.player(), 100)
            }

            /**
             * Skill-related logic.
             */
            if (it.player().getSkills().getMaxLevel(Skills.HITPOINTS) < 10) {
                it.player().getSkills().setBaseLevel(Skills.HITPOINTS, 10)
            }
            it.player().calculateAndSetCombatLevel()
            it.player().setInterfaceText(593, 1, "Unarmed")
            it.player().setInterfaceText(593, 2, "Combat Lvl: ${it.player().getSkills().combatLevel}")

            /**
             * Interface-related logic.
             */
            it.player().sendDisplayInterface(DisplayMode.FIXED)
            InterfacePane.values().filter { pane -> pane.interfaceId != -1 }.forEach { pane ->
                if (pane == InterfacePane.XP_COUNTER && it.player().getVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT) == 0) {
                    return@forEach
                } else if (pane == InterfacePane.MINI_MAP && it.player().getVarbit(OSRSGameframe.DATA_ORBS_HIDDEN_VARBIT) == 1) {
                    return@forEach
                }
                it.player().openInterface(pane.interfaceId, pane)
            }
            it.player().invokeScript(1105, 1) // Has display name


            /**
             * Game-related logic.
             */
            it.player().sendRunEnergy()
            it.player().message("Welcome to ${it.player().world.gameContext.name}.", OSRSMessageType.SERVER)
        }
    }
}