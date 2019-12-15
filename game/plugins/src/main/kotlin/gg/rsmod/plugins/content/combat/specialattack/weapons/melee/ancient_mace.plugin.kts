package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.content.combat.dealExactHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 100

SpecialAttacks.register(
        weapons = intArrayOf(Items.ANCIENT_MACE),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ATTACK,
        attack = {
            player.animate(id = 6147)
            player.graphic(id = 1052, height = 0)
            val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.0, ignore_protection = true )
            val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.0)
            var hitDamage = world.random(maxHit)
            var landHit = accuracy >= world.randomDouble()
            val delay = 1
            if (target.entityType.isPlayer && landHit && hitDamage>0) {
                (target as Player).getSkills().alterCurrentLevel(
                        skill = Skills.PRAYER,
                        value = -
                        (when (hitDamage >= (target as Player).getSkills().getCurrentLevel(Skills.PRAYER)) {
                            true -> (target as Player).getSkills().getCurrentLevel(Skills.PRAYER)
                            else -> hitDamage
                        }),
                        capValue = 0)
                player.getSkills().alterCurrentLevel(skill = Skills.PRAYER, value = hitDamage, capValue = hitDamage)
            }
            player.dealExactHit(target = target, exactHit = hitDamage, landHit = landHit, delay = delay)
        }
)