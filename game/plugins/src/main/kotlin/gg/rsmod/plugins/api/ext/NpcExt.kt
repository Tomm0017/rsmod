package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.combat.AttackStyle
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.combat.CombatStyle
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Projectile
import gg.rsmod.plugins.api.NpcSpecies

fun Npc.prepareAttack(combatClass: CombatClass, combatStyle: CombatStyle, attackStyle: AttackStyle) {
    this.combatClass = combatClass
    this.combatStyle = combatStyle
    this.attackStyle = attackStyle
}

fun Npc.createProjectile(target: Pawn, gfx: Int, startHeight: Int, endHeight: Int, delay: Int, angle: Int,
                         lifespan: Int = -1, steepness: Int = -1): Projectile {
    val start = getFrontFacingTile(target)
    val builder = Projectile.Builder()
            .setTiles(start = start, target = target)
            .setGfx(gfx = gfx)
            .setHeights(startHeight = startHeight, endHeight = endHeight)
            .setSlope(angle = angle, steepness = if (steepness == -1) Math.min(255, ((getSize() shr 1) + 1) * 32) else steepness)
            .setTimes(delay = delay, lifespan = if (lifespan == -1) (delay + (world.collision.raycastTiles(start, target.getCentreTile()) * 5)) else lifespan)

    return builder.build()
}

fun Npc.createProjectile(target: Tile, gfx: Int, startHeight: Int, endHeight: Int, delay: Int, angle: Int, lifespan: Int): Projectile {
    val builder = Projectile.Builder()
            .setTiles(start = getFrontFacingTile(target), target = target)
            .setGfx(gfx = gfx)
            .setHeights(startHeight = startHeight, endHeight = endHeight)
            .setSlope(angle = angle, steepness = Math.min(255, ((getSize() shr 1) + 1) * 32))
            .setTimes(delay = delay, lifespan = lifespan)

    return builder.build()
}

fun Npc.isSpecies(species: NpcSpecies, vararg others: NpcSpecies): Boolean = this.species.contains(species) || this.species.any { others.contains(it) }

fun Npc.getAttackBonus(): Int = equipmentBonuses[10]

fun Npc.getStrengthBonus(): Int = equipmentBonuses[11]

fun Npc.getRangedStrengthBonus(): Int = equipmentBonuses[12]

fun Npc.getMagicDamageBonus(): Int = equipmentBonuses[13]