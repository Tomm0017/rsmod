package gg.rsmod.plugins.content.combat.specialattack.weapons.ranged

import gg.rsmod.plugins.content.combat.createProjectile
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.RangedCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.combat.strategy.RangedCombatStrategy
import gg.rsmod.plugins.content.combat.strategy.ranged.RangedProjectile

val SPECIAL_REQUIREMENT = 55

SpecialAttacks.register(intArrayOf(Items.MAGIC_SHORTBOW, Items.MAGIC_SHORTBOW_I), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(1074)
    for (i in 0 until 2) {
        fireMagicArrow(player, target, i)
    }
}

 fun fireMagicArrow(player: Player, target : Pawn, extraDelay : Int = 0) {
    val world = player.world

    val ammo = player.getEquipment(EquipmentType.AMMO)

    val ammoProjectile = if (ammo != null) RangedProjectile.values.firstOrNull { it == RangedProjectile.MAGIC_ARROW } else null

    if (ammoProjectile != null) {

        val projectile = player.createProjectile(target, ammoProjectile.gfx, ammoProjectile.type, extraDelay = extraDelay*ammoProjectile.type.delay)

        ammoProjectile.drawback?.let {
            drawback ->
            drawback.delay = (extraDelay*ammoProjectile.type.delay)
            player.graphic(drawback)
        }
        world.spawn(projectile)

        world.spawn(AreaSound(tile = player.tile, id = 2693, radius = 10, volume = 1))

        val maxHit = RangedCombatFormula.getMaxHit(player, target)
        val accuracy = RangedCombatFormula.getAccuracy(player, target)
        val landHit = accuracy >= world.randomDouble()
        val delay = RangedCombatStrategy.getHitDelay(target.getCentreTile(), target.tile.transform(target.getSize() / 2, target.getSize() / 2))/2
        player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay +1)
    }
}