package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 50

SpecialAttacks.register(intArrayOf(Items.ABYSSAL_WHIP), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(id = 1658)
    world.spawn(AreaSound(tile = player.tile, id = 2713, radius = 10, volume = 1))

    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.0)
    val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.0)
    val landHit = accuracy >= world.randomDouble()

    if(landHit && target.entityType.isPlayer) {
        val drain = when ((target as Player).runEnergy > 10) { true -> 10.0 else -> (target as Player).runEnergy }
        (target as Player).runEnergy -= drain
        player.runEnergy += when(drain+player.runEnergy > 100.0) {true -> 100.0-player.runEnergy else -> drain}
    }
    target.graphic(341, 100)
    player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = 1)
}