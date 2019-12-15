package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 50

SpecialAttacks.register(intArrayOf(Items.ABYSSAL_BLUDGEON), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(id = 3299)
    target.graphic(id = 1284, height = 0)
    world.spawn(AreaSound(tile = player.tile, id = 2715, radius = 10, volume = 1))

    val prayerPointsMissing = (player.getSkills().getMaxLevel(Skills.PRAYER) - player.getSkills().getCurrentLevel(Skills.PRAYER))
    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.0 + prayerPointsMissing * 0.05)
    val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.25)
    val landHit = accuracy >= world.randomDouble()

    world.spawn(AreaSound(tile = target.tile, id = 1930, radius = 10, volume = 1))
    player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = 1)
}