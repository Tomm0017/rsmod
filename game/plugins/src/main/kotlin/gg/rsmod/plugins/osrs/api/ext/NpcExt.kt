package gg.rsmod.plugins.osrs.api.ext

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.combat.AttackStyle
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.combat.CombatStyle
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Projectile

/**
 * @author Tom <rspsmods@gmail.com>
 */
fun Npc.prepareAtttack(combatClass: CombatClass, combatStyle: CombatStyle, attackStyle: AttackStyle) {
    this.combatClass = combatClass
    this.combatStyle = combatStyle
    this.attackStyle = attackStyle
}

fun Npc.createProjectile(target: Pawn, gfx: Int, startHeight: Int, endHeight: Int, delay: Int, angle: Int, lifespan: Int): Projectile {
    val builder = Projectile.Builder()
            .setTiles(start = tile, target = target)
            .setGfx(gfx = gfx)
            .setHeights(startHeight = startHeight, endHeight = if (endHeight != -1) endHeight else endHeight)
            .setSlope(angle = angle, steepness = Math.min(255, ((getSize() shr 1) + 1) * 32))
            .setTimes(delay = delay, lifespan = lifespan)

    return builder.build()
}

fun Npc.createProjectile(target: Tile, gfx: Int, startHeight: Int, endHeight: Int, delay: Int, angle: Int, lifespan: Int): Projectile {
    val builder = Projectile.Builder()
            .setTiles(start = tile, target = target)
            .setGfx(gfx = gfx)
            .setHeights(startHeight = startHeight, endHeight = if (endHeight != -1) endHeight else endHeight)
            .setSlope(angle = angle, steepness = Math.min(255, ((getSize() shr 1) + 1) * 32))
            .setTimes(delay = delay, lifespan = lifespan)

    return builder.build()
}