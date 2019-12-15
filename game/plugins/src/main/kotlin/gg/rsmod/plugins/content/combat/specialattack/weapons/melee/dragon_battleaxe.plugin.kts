package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.inter.attack.AttackTab
import kotlin.math.floor

val SPECIAL_REQUIREMENT = 100

SpecialAttacks.register(
        weapons = intArrayOf(Items.DRAGON_BATTLEAXE),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ACTIVATION,
        attack = {
            player.graphic(id = 246, height = 0)
            player.animate(id = 1056)
            player.forceChat("Raarrrrrgggggghhhhhhh!")
            var drained = 0
            intArrayOf(Skills.ATTACK, Skills.MAGIC, Skills.RANGED, Skills.DEFENCE).forEach {
                skill ->
                val drainAmount = floor(player.getSkills().getMaxLevel(skill) / 10.0).toInt()
                drained += drainAmount
                player.getSkills().alterCurrentLevel(skill = skill, value = -drainAmount , capValue = 0)
            }
            player.getSkills().alterCurrentLevel(skill = Skills.STRENGTH, value = floor(drained / 4.0).toInt() +  floor(player.getSkills().getMaxLevel(Skills.STRENGTH) / 10.0).toInt() , capValue = 20)
        }
)