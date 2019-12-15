package gg.rsmod.plugins.content.combat.specialattack.weapons.ranged

import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.content.combat.createProjectile
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.RangedCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.combat.strategy.RangedCombatStrategy
import gg.rsmod.plugins.content.combat.strategy.ranged.RangedProjectile

val SPECIAL_REQUIREMENT = 40

SpecialAttacks.register(intArrayOf(Items.ARMADYL_CROSSBOW), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(4230)
    fireArmadylBolt(player, target)
}

fun fireArmadylBolt(player: Player, target : Pawn) {
    val world = player.world

    val ammo = player.getEquipment(EquipmentType.AMMO)
    val ammoProjectile = if (ammo != null) RangedProjectile.values.firstOrNull { it == RangedProjectile.ARMADYL_BOLT } else null

    if (ammoProjectile != null) {

        ammoProjectile.drawback?.let { drawback -> player.graphic(drawback) }
        val projectile = player.createProjectile(target, ammoProjectile.gfx, ammoProjectile.type)

        world.spawn(projectile)
        world.spawn(AreaSound(tile = player.tile, id = 2693, radius = 10, volume = 1))

        val maxHit = RangedCombatFormula.getMaxHit(player, target)
        val accuracy = RangedCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 2.0)
        val landHit = accuracy >= world.randomDouble()
        val delay = RangedCombatStrategy.getHitDelay(target.getCentreTile(), target.tile.transform(target.getSize() / 2, target.getSize() / 2))
        player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
    }

}