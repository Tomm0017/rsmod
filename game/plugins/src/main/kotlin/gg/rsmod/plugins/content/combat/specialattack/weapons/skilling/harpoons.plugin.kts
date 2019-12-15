package gg.rsmod.plugins.content.combat.specialattack.weapons.skilling
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.inter.attack.AttackTab

val SPECIAL_REQUIREMENT = 100

SpecialAttacks.register(
        weapons = intArrayOf(Items.DRAGON_HARPOON),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ACTIVATION,
        attack = {
            player.animate(id = 1056)
            player.graphic(id = 246, height = 0)
            player.forceChat("Here fishy fishies!!")
            player.getSkills().alterCurrentLevel(skill = Skills.FISHING, value = 3 , capValue = 3)
        }
)