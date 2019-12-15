package gg.rsmod.plugins.content.combat.specialattack.weapons.skilling
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.inter.attack.AttackTab

val SPECIAL_REQUIREMENT = 100

SpecialAttacks.register(
        weapons = intArrayOf(Items.DRAGON_PICKAXE,Items.INFERNAL_PICKAXE, Items._3RD_AGE_PICKAXE, Items.CRYSTAL_PICKAXE),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ACTIVATION,
        attack = {
            player.animate(id = 2876)//need correct anim
            player.graphic(id = 479, height = 100)//need correct gfx
            player.forceChat("Smashing!")
            player.getSkills().alterCurrentLevel(skill = Skills.MINING, value = 3 , capValue = 3)
        }
)