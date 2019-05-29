package gg.rsmod.plugins.content.combat.specialattack.weapons.dragondagger

import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 25

SpecialAttacks.register(Items.DRAGON_DAGGER, SPECIAL_REQUIREMENT) {
    player.animate(id = 1062)
    player.graphic(id = 252, height = 92)
    world.spawn(AreaSound(tile = player.tile, id = 2537, radius = 10, volume = 1))

    for (i in 0 until 2) {
        val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.15)
        val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.25)
        val landHit = accuracy >= world.randomDouble()
        val delay = if (target.entityType.isNpc) i + 1 else 1
        player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
    }
}