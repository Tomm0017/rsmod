package gg.rsmod.plugins.content.combat.specialattack.weapons.ranged
import gg.rsmod.game.model.entity.AreaSound
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.getEquipment
import gg.rsmod.plugins.content.combat.CombatConfigs
import gg.rsmod.plugins.content.combat.createProjectile
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.formula.RangedCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.combat.strategy.RangedCombatStrategy
import gg.rsmod.plugins.content.combat.strategy.ranged.RangedProjectile
import gg.rsmod.plugins.content.mechanics.prayer.Prayers

val SPECIAL_REQUIREMENT = 25

SpecialAttacks.register(intArrayOf(Items.DRAGON_THROWNAXE), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(7521)
    throwDragonAxe(player, target)
}

fun throwDragonAxe(player: Player, target : Pawn) {
    val world = player.world

    val ammo = player.getEquipment(EquipmentType.AMMO)

    val ammoProjectile = if (ammo != null) RangedProjectile.values.firstOrNull {  it == RangedProjectile.DRAGON_THROWING_AXE_SPEC } else null

    if (ammoProjectile != null) {
        val projectile = player.createProjectile(target, ammoProjectile.gfx, ammoProjectile.type)
        ammoProjectile.drawback?.let { drawback -> player.graphic(drawback) }
        world.spawn(projectile)

        world.spawn(AreaSound(tile = player.tile, id = 2693, radius = 10, volume = 1))

        val maxHit = RangedCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.25)
        val accuracy = RangedCombatFormula.getAccuracy(player, target)
        val landHit = accuracy >= world.randomDouble()
        val delay = RangedCombatStrategy.getHitDelay(target.getCentreTile(), target.tile.transform(target.getSize() / 2, target.getSize() / 2))
        player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
    }

}