package gg.rsmod.plugins.content.inter.skillguides

val SKILL_ID_VARBIT = 4371
val SUB_SECTION_VARBIT = 4372

SkillGuide.values.forEach { guide ->
    on_button(320, guide.child) {
        if (!player.lock.canInterfaceInteract()) {
            return@on_button
        }

        player.setVarbit(SUB_SECTION_VARBIT, 0)
        player.setVarbit(SKILL_ID_VARBIT, guide.bit)
        player.setInterfaceEvents(interfaceId = 214, component = 25, from = -1, to = -1, setting = 0)
        player.setInterfaceUnderlay(color = -1, transparency = -1)
        player.openInterface(interfaceId = 214, dest = InterfaceDestination.MAIN_SCREEN)
    }
}

for (section in 11..24) {
    on_button(214, section) {
        player.setVarbit(SUB_SECTION_VARBIT, section - 11)
    }
}