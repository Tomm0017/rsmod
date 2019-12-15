package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 30


SpecialAttacks.register(
        weapons = intArrayOf(Items.DRAGON_HALBERD),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ATTACK,
        attack = {
            player.animate(id = 1203)
            player.graphic(id = 1231, height = 100)
            val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.10)
            val delay = 1
            if (target.getSize() > 1) {
                val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 0.75)
                val landHit = accuracy >= world.randomDouble()
                player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
            }
            val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.0)
            val landHit = accuracy >= world.randomDouble()
            player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
        }
)