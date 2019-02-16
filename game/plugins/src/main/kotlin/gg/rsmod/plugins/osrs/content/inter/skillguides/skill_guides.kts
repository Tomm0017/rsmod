package gg.rsmod.plugins.osrs.content.inter.skillguides

import gg.rsmod.plugins.osrs.api.InterfaceDestination
import gg.rsmod.plugins.osrs.api.ext.*

val SKILL_ID_VARBIT = 4371
val SUB_SECTION_VARBIT = 4372

SkillGuide.values.forEach { guide ->
    on_button(320, guide.child) {
        val p = it.player()

        if (!p.lock.canComponentInteract()) {
            return@on_button
        }

        p.setVarbit(SUB_SECTION_VARBIT, 0)
        p.setVarbit(SKILL_ID_VARBIT, guide.bit)
        p.setInterfaceEvents(interfaceId = 214, component = 25, from = -1, to = -1, setting = 0)
        p.setInterfaceUnderlay(color = -1, transparency = -1)
        p.openInterface(interfaceId = 214, dest = InterfaceDestination.MAIN_SCREEN)
    }
}

for (section in 11..24) {
    on_button(214, section) {
        it.player().setVarbit(SUB_SECTION_VARBIT, section - 11)
    }
}