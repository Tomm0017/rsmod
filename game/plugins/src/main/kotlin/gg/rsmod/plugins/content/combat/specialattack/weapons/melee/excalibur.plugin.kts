package gg.rsmod.plugins.content.combat.specialattack.weapons.melee
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.inter.attack.AttackTab

val SPECIAL_REQUIREMENT = 100

SpecialAttacks.register(
        weapons = intArrayOf(Items.EXCALIBUR),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ACTIVATION,
        attack = {
            AttackTab.disableSpecial(player)
            player.animate(id = 1057)
            player.graphic(id = 247, height = 0)
            player.forceChat("For Camelot!")
            player.getSkills().alterCurrentLevel(skill = Skills.DEFENCE, value = 8 , capValue = 8)
        }
)