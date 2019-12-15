package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 40

SpecialAttacks.register(intArrayOf(Items.DRAGON_SWORD), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(id = 7515)
    player.graphic(id = 1369, height = 100)
    //world.spawn(AreaSound(tile = player.tile, id = 2537, radius = 10, volume = 1))

    for (i in 0 until 1) {
        val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.25, ignore_protection = true)
        val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.25)
        val landHit = accuracy >= world.randomDouble()
        val delay = if (target.entityType.isNpc) i + 1 else 1
        player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
    }
}