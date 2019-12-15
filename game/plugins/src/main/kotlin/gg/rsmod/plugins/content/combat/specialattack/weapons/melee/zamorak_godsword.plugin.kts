package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.game.model.entity.AreaSound
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.freeze
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 50

SpecialAttacks.register(intArrayOf(Items.ZAMORAK_GODSWORD), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(id = 7639)
    player.graphic(id = 1210, height = 0)
    world.spawn(AreaSound(tile = player.tile, id = 3869, radius = 10, volume = 1))

    for (i in 0 until 1) {
        val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.10)
        val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 2.0)
        val landHit = accuracy >= world.randomDouble()
        val delay = if (target.entityType.isNpc) i + 1 else 1
        target.freeze(34) {target.graphic(369)}
        player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
    }
}