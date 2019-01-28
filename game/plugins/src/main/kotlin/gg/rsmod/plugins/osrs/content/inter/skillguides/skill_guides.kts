
import gg.rsmod.plugins.osrs.api.ComponentPane
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.skillguides.SkillGuide

val skillIdVarbit = 4371
val subsectionVarbit = 4372

SkillGuide.values().forEach { guide ->
    onButton(320, guide.child) {
        val p = it.player()

        if (!p.lock.canComponentInteract()) {
            return@onButton
        }

        p.setVarbit(subsectionVarbit, 0)
        p.setVarbit(skillIdVarbit, guide.bit)
        p.setComponentSetting(parent = 214, child = 25, from = -1, to = -1, setting = 0)
        p.setComponentUnderlay(color = -1, transparency = -1)
        p.openComponent(component = 214, pane = ComponentPane.MAIN_SCREEN)
    }
}

for (section in 11..24) {
    onButton(214, section) {
        it.player().setVarbit(subsectionVarbit, section - 11)
    }
}