package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.content.combat.dealExactHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 50
val drainSkills = intArrayOf(Skills.DEFENCE,Skills.STRENGTH, Skills.PRAYER, Skills.ATTACK, Skills.MAGIC, Skills.RANGED)


SpecialAttacks.register(
        weapons = intArrayOf(Items.BANDOS_GODSWORD),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ATTACK,
        attack = {
            player.graphic(id = 1212, height = 0)
            player.animate(id = 7643)
            val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.21)
            val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 2.0)
            var hitDamage = world.random(maxHit)
            var landHit = accuracy >= world.randomDouble()
            val delay = 1
            var drain = hitDamage
            if (target.entityType.isPlayer && landHit && hitDamage>0)
                drainSkills.forEach { skill ->
                    if (drain > 0) {
                        (target as Player).getSkills().alterCurrentLevel(skill = skill, value = -(when (drain >= (target as Player).getSkills().getCurrentLevel(skill)) {
                            true -> (target as Player).getSkills().getCurrentLevel(skill)
                            else -> drain
                        }), capValue = 0)
                        drain -= player.getSkills().getCurrentLevel(skill)
                    }
                }
            player.dealExactHit(target = target, exactHit = hitDamage, landHit = landHit, delay = delay)
        }
)