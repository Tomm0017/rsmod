package gg.rsmod.plugins.content.combat.specialattack.weapons.ranged

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.content.combat.createProjectile
import gg.rsmod.plugins.content.combat.dealExactHit
import gg.rsmod.plugins.content.combat.formula.RangedCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.combat.strategy.RangedCombatStrategy
import gg.rsmod.plugins.content.combat.strategy.ranged.RangedProjectile
import gg.rsmod.plugins.content.combat.strategy.ranged.weapon.CrossbowType

val SPECIAL_REQUIREMENT = 75

SpecialAttacks.register(intArrayOf(Items.DORGESHUUN_CROSSBOW), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(4230)
    val hitDamage = fireDorgueshuunBolt(player, target)
    if(hitDamage > 0) {
        player.getSkills().alterCurrentLevel(skill = Skills.DEFENCE, value = -hitDamage , capValue = 0)
    }
}

fun fireDorgueshuunBolt(player: Player, target : Pawn):Int  {
    val world = player.world

    val weapon = player.getEquipment(EquipmentType.WEAPON)

    val crossbow = CrossbowType.values.firstOrNull { it.item == weapon?.id }
    val ammo = player.getEquipment(EquipmentType.AMMO)
    val ammoProjectile = if (ammo != null) RangedProjectile.values.firstOrNull { it == RangedProjectile.DORGESHUUN_BOLT } else null

    if (ammoProjectile != null) {

        ammoProjectile.drawback?.let { drawback -> player.graphic(drawback) }
        val projectile = player.createProjectile(target, ammoProjectile.gfx, ammoProjectile.type)

        world.spawn(projectile)
        world.spawn(AreaSound(tile = player.tile, id = 2693, radius = 10, volume = 1))

        val maxHit = RangedCombatFormula.getMaxHit(player, target)
        val accuracy = RangedCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 2.0)
        val hitDamage = world.random(maxHit)
        val landHit = accuracy >= world.randomDouble()
        val delay = RangedCombatStrategy.getHitDelay(target.getCentreTile(), target.tile.transform(target.getSize() / 2, target.getSize() / 2))
        player.dealExactHit(target = target, exactHit = hitDamage, landHit = landHit, delay = delay)
        return hitDamage
    }
    return 0

}