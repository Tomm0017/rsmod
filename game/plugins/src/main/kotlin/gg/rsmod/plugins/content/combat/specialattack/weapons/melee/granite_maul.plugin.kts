package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.game.model.entity.AreaSound
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 60

SpecialAttacks.register(intArrayOf(Items.GRANITE_MAUL), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.graphic(id = 340, height = 92)
    player.animate(id = 1667)
    world.spawn(AreaSound(tile = player.tile, id = 2715, radius = 10, volume = 1))//sound for gmaul?
    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.00)
    val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.00)
    val landHit = accuracy >= world.randomDouble()
    player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = 0)
}