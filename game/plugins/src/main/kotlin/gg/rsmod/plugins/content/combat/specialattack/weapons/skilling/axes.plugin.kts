package gg.rsmod.plugins.content.combat.specialattack.weapons.skilling

import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.inter.attack.AttackTab

val SPECIAL_REQUIREMENT = 100

SpecialAttacks.register(intArrayOf(Items.DRAGON_AXE,Items.INFERNAL_AXE, Items._3RD_AGE_AXE, Items.CRYSTAL_AXE), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ACTIVATION) {
            player.animate(id = 2876)
            player.graphic(id = 479, height = 100)
            player.forceChat("Chop chop!")
            player.getSkills().alterCurrentLevel(skill = Skills.WOODCUTTING, value = 3 , capValue = 3)
        }

