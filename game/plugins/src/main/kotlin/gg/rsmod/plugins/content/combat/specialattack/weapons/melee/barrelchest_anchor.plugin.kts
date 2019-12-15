package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.content.combat.dealExactHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 50
val drainSkills = intArrayOf(Skills.DEFENCE, Skills.ATTACK, Skills.MAGIC, Skills.RANGED)


SpecialAttacks.register(
        weapons = intArrayOf(Items.BARRELCHEST_ANCHOR),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ATTACK,
        attack = {
            player.animate(id = 6147)
            player.graphic(id = 1027, height = 0)
            val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.10)
            val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 2.0)
            var hitDamage = world.random(maxHit)
            var landHit = accuracy >= world.randomDouble()
            val delay = 1
            if (target.entityType.isPlayer && landHit && hitDamage>0) {
                    if (hitDamage > 0) {
                        val drainSkill = drainSkills.random()
                        (target as Player).getSkills().alterCurrentLevel(skill = drainSkill, value = -((target as Player).getSkills().getMaxLevel(drainSkill)/10), capValue = 0)
                    }
                }
            player.dealExactHit(target = target, exactHit = hitDamage, landHit = landHit, delay = delay)
        }
)