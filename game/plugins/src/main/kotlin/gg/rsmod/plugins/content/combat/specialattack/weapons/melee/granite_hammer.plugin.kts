package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.content.combat.dealExactHit
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import kotlin.math.floor

val SPECIAL_REQUIREMENT = 60

SpecialAttacks.register(intArrayOf(Items.GRANITE_HAMMER), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(id = 1378)
    player.graphic(id = 1450, height = 0)
    world.spawn(AreaSound(tile = player.tile, id = 2541, radius = 10, volume = 1))

    for (i in 0 until 1) {
        val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.5)
        val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.0)
        var hitDamage = world.random(maxHit)
        val landHit = accuracy >= world.randomDouble()
        val delay = if (target.entityType.isNpc) i + 1 else 1
        player.dealExactHit(target = target, exactHit = hitDamage+5, landHit = landHit, delay = delay)
    }
}