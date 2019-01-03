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
            p.invokeScript(1105, 1) // Has display name
            p.invokeScript(2494, 1) // Has display name

            /**
             * Game-related logic.
             */
            p.sendRunEnergy()
            p.message("Welcome to ${p.world.gameContext.name}.", OSRSMessageType.SERVER)
        }
    }
}