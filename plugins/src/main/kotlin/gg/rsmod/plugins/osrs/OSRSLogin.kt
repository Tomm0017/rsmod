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
            val p = it.player()

            /**
             * First log-in logic (when accounts have just been made).
             */
            if (p.attr.getOrDefault(NEW_ACCOUNT_ATTR, false)) {
                SpecialEnergy.setEnergy(p, 100)
            }

            /**
             * Skill-related logic.
             */
            if (p.getSkills().getMaxLevel(Skills.HITPOINTS) < 10) {
                p.getSkills().setBaseLevel(Skills.HITPOINTS, 10)
            }
            p.calculateAndSetCombatLevel()
            p.setInterfaceText(593, 1, "Unarmed")
            p.setInterfaceText(593, 2, "Combat Lvl: ${p.getSkills().combatLevel}")

            /**
             * Interface-related logic.
             */
            p.sendDisplayInterface(DisplayMode.FIXED)
            InterfacePane.values().filter { pane -> pane.interfaceId != -1 }.forEach { pane ->
                if (pane == InterfacePane.XP_COUNTER && p.getVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT) == 0) {
                    return@forEach
                } else if (pane == InterfacePane.MINI_MAP && p.getVarbit(OSRSGameframe.DATA_ORBS_HIDDEN_VARBIT) == 1) {
                    return@forEach
                }
                p.openInterface(pane.interfaceId, pane)
            }

            /**
             * Inform the client whether or not we have a display name.
             */
            val displayName = p.username.isNotEmpty()
            p.invokeScript(1105, if (displayName) 1 else 0) // Has display name
            p.invokeScript(423, p.username)
            if (p.getVarp(1055) == 0 && displayName) {
                p.syncVarp(1055)
            }

            /**
             * Game-related logic.
             */
            p.sendRunEnergy()
            p.message("Welcome to ${p.world.gameContext.name}.", OSRSMessageType.SERVER)
        }
    }
}