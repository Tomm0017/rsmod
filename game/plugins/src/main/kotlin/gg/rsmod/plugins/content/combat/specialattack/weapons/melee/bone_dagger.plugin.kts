package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.content.combat.dealExactHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 50

SpecialAttacks.register(
        weapons = intArrayOf(Items.BONE_DAGGER),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ATTACK,
        attack = {
            player.animate(id = 4198)
            player.graphic(id = 705, height = 0)
            val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.0)
            val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.0)
            var hitDamage = world.random(maxHit)
            var landHit = accuracy >= world.randomDouble()
            val delay = 1
            if (target.entityType.isPlayer && landHit && hitDamage>0) {
                (target as Player).getSkills().alterCurrentLevel(skill = Skills.DEFENCE, value = -hitDamage, capValue = 0)
            }
            player.dealExactHit(target = target, exactHit = hitDamage, landHit = landHit, delay = delay)
        }
)