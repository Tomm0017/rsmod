package gg.rsmod.plugins.osrs.content.inter

import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.*
import gg.rsmod.plugins.osrs.InterfacePane

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SkillGuides {

    private const val SKILL_ID_VARBIT = 4371
    private const val SUBSECTION_VARBIT = 4372

    private enum class SkillGuide(val child: Int, val bit: Int) {
        ATTACK(child = 1, bit = 1),
        STRENGTH(child = 2, bit = 2),
        DEFENCE(child = 3, bit = 5),
        RANGED(child = 4, bit = 3),
        PRAYER(child = 5, bit = 7),
        MAGIC(child = 6, bit = 4),
        RUNECRAFTING(child = 7, bit = 12),
        CONSTRUCTION(child = 8, bit = 22),
        HITPOINTS(child = 9, bit = 6),
        AGILITY(child = 10, bit = 8),
        HERBLORE(child = 11, bit = 9),
        THIEVING(child = 12, bit = 10),
        CRAFTING(child = 13, bit = 11),
        FLETCHING(child = 14, bit = 19),
        SLAYER(child = 15, bit = 20),
        HUNTER(child = 16, bit = 23),
        MINING(child = 17, bit = 13),
        SMITHING(child = 18, bit = 14),
        FISHING(child = 19, bit = 15),
        COOKING(child = 20, bit = 16),
        FIREMAKING(child = 21, bit = 17),
        WOODCUTTING(child = 22, bit = 18),
        FARMING(child = 23, bit = 21)
    }

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        SkillGuide.values().forEach { guide ->
            r.bindButton(320, guide.child) {
                val p = it.player()

                p.setVarbit(SUBSECTION_VARBIT, 0)
                p.setVarbit(SKILL_ID_VARBIT, guide.bit)
                p.setInterfaceSetting(parent = 214, child = 25, from = -1, to = -1, setting = 0)
                p.setMainInterfaceBackground(color = -1, transparency = -1)
                p.openInterface(interfaceId = 214, pane = InterfacePane.MAIN_SCREEN)
            }
        }

        for (section in 11..24) {
            r.bindButton(214, section) {
                it.player().setVarbit(SUBSECTION_VARBIT, section - 11)
            }
        }
    }
}