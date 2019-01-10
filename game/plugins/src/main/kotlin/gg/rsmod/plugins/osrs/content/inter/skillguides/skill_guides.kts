import gg.rsmod.plugins.*
import gg.rsmod.plugins.osrs.InterfacePane
import gg.rsmod.plugins.osrs.content.inter.skillguides.SkillGuide

/**
 * @author Tom <rspsmods@gmail.com>
 */

val skillIdVarbit = 4371
val subsectionVarbit = 4372

SkillGuide.values().forEach { guide ->
    r.bindButton(320, guide.child) {
        val p = it.player()

        p.setVarbit(subsectionVarbit, 0)
        p.setVarbit(skillIdVarbit, guide.bit)
        p.setInterfaceSetting(parent = 214, child = 25, from = -1, to = -1, setting = 0)
        p.setMainInterfaceBackground(color = -1, transparency = -1)
        p.openInterface(interfaceId = 214, pane = InterfacePane.MAIN_SCREEN)
    }
}

for (section in 11..24) {
    r.bindButton(214, section) {
        it.player().setVarbit(subsectionVarbit, section - 11)
    }
}