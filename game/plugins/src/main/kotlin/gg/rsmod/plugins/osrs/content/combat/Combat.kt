package gg.rsmod.plugins.osrs.content.combat

import gg.rsmod.game.action.NpcPathAction
import gg.rsmod.game.model.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Projectile
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.ProjectileType

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Combat {

    val ATTACK_DELAY = TimerKey()

    const val PRIORITY_PID_VARP = 1075

    fun reset(pawn: Pawn) {
        pawn.attr.remove(COMBAT_TARGET_FOCUS_ATTR)
    }

    fun raycast(pawn: Pawn, target: Pawn, distance: Int): Boolean {
        val world = pawn.world
        val start = pawn.calculateCentreTile()
        val end = target.calculateCentreTile()

        return start.isWithinRadius(end, distance) && world.collision.raycast(start, end, projectile = true)
    }

    suspend fun moveToAttackRange(it: Plugin, pawn: Pawn, target: Pawn, distance: Int): Boolean {
        val world = pawn.world
        val start = pawn.calculateCentreTile()
        val end = target.calculateCentreTile()

        val withinRange = start.isWithinRadius(end, distance) && world.collision.raycast(start, end, projectile = true)
        return withinRange || NpcPathAction.walkTo(it, pawn, target, interactionRange = distance)
    }

    fun createProjectile(source: Pawn, target: Tile, gfx: Int, type: ProjectileType): Projectile {
        val distance = source.calculateCentreTile().getDistance(target)

        val builder = Projectile.Builder()
                .setTiles(start = source.calculateCentreTile(), target = target)
                .setGfx(gfx = gfx)
                .setHeights(startHeight = type.startHeight, endHeight = type.endHeight)
                .setSlope(angle = type.angle, steepness = type.steepness)
                .setTimes(delay = type.delay, lifespan = type.delay + type.calculateLife(distance))

        return builder.build()
    }

    fun createProjectile(source: Pawn, target: Pawn, gfx: Int, type: ProjectileType): Projectile {
        val distance = source.calculateCentreTile().getDistance(target.calculateCentreTile())
        val builder = Projectile.Builder()
                .setTiles(start = source.calculateCentreTile(), target = target)
                .setGfx(gfx = gfx)
                .setHeights(startHeight = type.startHeight, endHeight = type.endHeight)
                .setSlope(angle = type.angle, steepness = type.steepness)
                .setTimes(delay = type.delay, lifespan = type.delay + type.calculateLife(distance))

        return builder.build()
    }
}