package gg.rsmod.game.action

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.plugin.Plugin

/**
 * @author Tom <rspsmods@gmail.com>
 */
object CombatPathAction {

    suspend fun walkTo(it: Plugin, pawn: Pawn, target: Pawn, attackRange: Int): Boolean {
        var start = pawn.calculateCentreTile()
        var end = target.calculateCentreTile()
        val targetRadius = Math.floor(target.getTileSize() / 2.0).toInt()
        val world = pawn.world

        /**
         * The minimum distance to initiate attack.
         */
        val range = Math.max(targetRadius + 1, attackRange)

        /**
         * If the attack range is greater than 1, we assume we need to find a
         * path for projectiles instead.
         *
         * The difference lies in that projectile pathing will allow for objects
         * that allow projectiles to stand in the way, instead of completely
         * blocking them out as a valid path.
         */
        val projectile = attackRange > 1

        /**
         * The tile that the player will walk towards.
         */
        val dst: Tile

        if (start.isWithinRadius(end, targetRadius)) {
            if (targetRadius == 0) {
                val order = Direction.WNES
                val validTiles = order.map { end.step(it) }
                val tile = validTiles.firstOrNull { world.collision.canTraverse(start, Direction.between(end, it), pawn.getType()) } ?: return false
                dst = pawn.walkTo(tile.x, tile.z, MovementQueue.StepType.NORMAL, projectilePath = projectile) ?: return false
            } else {
                val borderTiles = getBorderTiles(target)
                val tile = borderTiles.sortedBy { tile -> tile.getDistance(pawn.tile) }.firstOrNull { world.collision.raycast(it, end, projectile = false) } ?: return false
                dst = pawn.walkTo(tile.x, tile.z, MovementQueue.StepType.NORMAL, projectilePath = projectile) ?: return false
                if (dst == pawn.tile) {
                    return false
                }
            }
        } else {
            val borderTiles = getBorderTiles(target)
            val shortestPath = pawn.createPathingStrategy().getPath(start, end, pawn.getType(), validSurroundingTiles = borderTiles)
            var tail = shortestPath.poll()
            while (tail != null) {
                val poll = shortestPath.poll() ?: break
                tail = poll
            }
            val minDistance = borderTiles.minBy { tile -> tile.getDistance(tail) + tile.getDelta(start) } ?: return false
            val bestScore = minDistance.getDistance(tail) + minDistance.getDelta(start)

            val closest = borderTiles.filter { tile -> tile.getDistance(tail) + tile.getDelta(start) == bestScore }
                    .sortedBy { tile -> tile.getDelta(start) }.firstOrNull { world.collision.raycast(it, end, projectile = false) } ?: return false

            if (pawn.tile.sameAs(closest)) {
                return true
            }
            dst = pawn.walkTo(closest.x, closest.z, MovementQueue.StepType.NORMAL, projectilePath = projectile) ?: return false
        }

        while (!pawn.tile.sameAs(dst)) {
            if (target.lastTile?.sameAs(target.tile) == false) {
                return walkTo(it, pawn, target, attackRange)
            }

            start = pawn.calculateCentreTile()
            end = target.calculateCentreTile()

            if (!start.isWithinRadius(end, targetRadius) && start.isWithinRadius(end, range) && world.collision.raycast(start, end, projectile = true)) {
                return true
            }

            it.wait(1)
        }

        start = pawn.calculateCentreTile()
        end = target.calculateCentreTile()

        return start.isWithinRadius(end, range) && world.collision.raycast(start, end, projectile = true)
    }

    private fun getBorderTiles(target: Pawn): Array<Tile> {
        val tiles = arrayListOf<Tile>()

        val size = target.getTileSize()

        for (z in -1..size) {
            for (x in -1..size) {
                /**
                 * Tiles inside the target aren't valid.
                 */
                if (x in 0 until size && z in 0 until size) {
                    continue
                }
                /**
                 * Diagonal tiles aren't valid!
                 */
                val southWest = x == -1 && z == -1
                val northWest = x == -1 && z == size
                val southEast = x == size && z == -1
                val northEast = x == size && z == size
                if (southWest || northWest || southEast || northEast) {
                    continue
                }

                val tile = target.tile.transform(x, z)
                tiles.add(tile)
            }
        }

        return tiles.toTypedArray()
    }
}