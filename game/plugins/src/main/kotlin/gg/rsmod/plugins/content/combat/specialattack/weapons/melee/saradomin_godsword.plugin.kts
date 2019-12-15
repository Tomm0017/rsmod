package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.dealExactHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 50

SpecialAttacks.register(
        weapons = intArrayOf(Items.SARADOMIN_GODSWORD),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ATTACK,
        attack = {
            player.graphic(id = 1209, height = 0)
            player.animate(id = 7641)
            val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.21)
            val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 2.0)
            var hitDamage = world.random(maxHit)
            var landHit = accuracy >= world.randomDouble()
            val delay = 1
            if (landHit && hitDamage > 0) {
                player.heal((when(hitDamage > 21) {true -> hitDamage/2 else -> 10}),  0)
                player.getSkills().alterCurrentLevel(skill = Skills.PRAYER, value = (when(hitDamage > 21) {true -> hitDamage/4 else -> 5}), capValue = 0)
            }
            player.dealExactHit(target = target, exactHit = hitDamage, landHit = landHit, delay = delay)
        }
)