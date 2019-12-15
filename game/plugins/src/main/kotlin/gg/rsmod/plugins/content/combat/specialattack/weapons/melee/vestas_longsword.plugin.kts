package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.content.combat.dealExactHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import kotlin.math.floor

val SPECIAL_REQUIREMENT = 25

SpecialAttacks.register(intArrayOf(Items.VESTAS_LONGSWORD), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(id = 7515)
    //TODO FIND GFX for vesta sword spec?

    for (i in 0 until 1) {
        val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.5)
        val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 4.0)
        var hitDamage = ((maxHit*1/5)..(maxHit*6/5)).random()
        val landHit = accuracy >= world.randomDouble()
        val delay = if (target.entityType.isNpc) i + 1 else 1
        if(landHit && hitDamage > 0)
            (target as Player).getSkills().alterCurrentLevel(skill = Skills.DEFENCE, value = -floor((target as Player).getSkills().getMaxLevel(Skills.DEFENCE) * 30.0/100.0).toInt() , capValue = 0)
        player.dealExactHit(target = target, exactHit = hitDamage, landHit = landHit, delay = delay)
    }
}