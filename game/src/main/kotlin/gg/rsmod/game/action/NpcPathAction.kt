package gg.rsmod.game.action

import gg.rsmod.game.message.impl.SetMinimapMarkerMessage
import gg.rsmod.game.model.*
import gg.rsmod.game.model.INTERACTING_NPC_ATTR
import gg.rsmod.game.model.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin

/**
 * @author Tom <rspsmods@gmail.com>
 */
object NpcPathAction {

    val walkPlugin: (Plugin) -> Unit = {
        val pawn = it.ctx as Pawn
        val world = pawn.world
        val npc = pawn.attr[INTERACTING_NPC_ATTR]!!
        val opt = pawn.attr[INTERACTING_OPT_ATTR]!!

        it.suspendable {
            pawn.facePawn(npc)

            val pathFound = walkTo(it, pawn, npc, interactionRange = 1)

            if (!pathFound) {
                pawn.movementQueue.clear()
                if (pawn is Player) {
                    if (!pawn.timers.has(FROZEN_TIMER)) {
                        pawn.message(Entity.YOU_CANT_REACH_THAT)
                    }
                    pawn.write(SetMinimapMarkerMessage(255, 255))
                }
                pawn.facePawn(null)
                return@suspendable
            }

            if (pawn is Player) {
                val handled = world.plugins.executeNpc(pawn, npc.id, opt)
                if (!handled) {
                    pawn.message(Entity.NOTHING_INTERESTING_HAPPENS)
                }
            }
        }
    }

    suspend fun walkTo(it: Plugin, pawn: Pawn, target: Pawn, interactionRange: Int): Boolean {
        var start = pawn.calculateCentreTile()
        var end = target.calculateCentreTile()

        val pawnSize = pawn.getTileSize() - 1
        val targetSize = target.getTileSize() - 1
        val targetRadius = Math.floor(targetSize / 2.0).toInt()
        val border = getBorderTiles(target)

        val world = pawn.world

        /**
         * The minimum distance to interact with [target].
         */
        val range = Math.max(targetRadius + 1, interactionRange)

        /**
         * If the interaction range is greater than 2, we assume we need to find a
         * path for projectiles instead. This value is 2 because halberds have an
         * interaction range of 2 tiles, but is not considered a projectile.
         *
         * The difference lies in that projectile pathing will allow for objects
         * that allow projectiles to stand in the way, instead of completely
         * blocking them out as a valid path.
         */
        val projectile = interactionRange > 2
        val distantInteraction = interactionRange > 1

        /**
         * Check to see if [pawn] is currently standing in a diagonal tile and
         * both [pawn] and [target] have a tile size of 1. You shouldn't be able
         * to interact with [target] while standing diagonally from the target.
         */
        val diagonal = !distantInteraction && targetRadius == 0 && pawn.getTileSize() == 1 && start.isWithinRadius(end, 1) && Direction.between(start, end).isDiagonal()

        /**
         * The tile that the player will walk towards.
         */
        var dst: Tile? = null

        /**
         * If the player is within interaction range and raycast is successful,
         * let's just return true.
         */
        var valid = if (!distantInteraction) pawn.tile in border else !overlap(pawn.tile, pawnSize, target.tile, targetSize) && start.isWithinRadius(end, range)
        if (valid && !diagonal && world.collision.raycast(start, end, projectile = projectile)) {
            return true
        }

        /**
         * If the player is frozen and not within interaction range, they can't
         * do anything.
         */
        if (pawn.timers.has(FROZEN_TIMER)) {
            if (pawn is Player) {
                pawn.message("A magical force stops you from moving.")
            }
            return false
        }

        /**
         * Check to see if the [pawn] is inside of the [target]. If so, we have
         * to move outwards.
         */
        if (overlap(pawn.tile, pawnSize, target.tile, targetSize)) {
            /**
             * If the target has a size of 1 (radius of 0) then we just do a quick
             * calculation to move out of their way. On 07, the west side has priority
             * when moving outwards.
             */
            if (targetRadius == 0) {
                val order = Direction.WNES
                val validTiles = order.map { end.step(it) }
                val tile = validTiles.firstOrNull { world.collision.canTraverse(start, Direction.between(end, it), pawn.getType()) } ?: return false
                dst = pawn.walkTo(tile.x, tile.z, MovementQueue.StepType.NORMAL, projectilePath = distantInteraction) ?: return false
            } else {
                val tile = border.sortedBy { tile -> tile.getDistance(pawn.tile) }.firstOrNull { world.collision.raycast(it, end, projectile = false) } ?: return false
                dst = pawn.walkTo(tile.x, tile.z, MovementQueue.StepType.NORMAL, projectilePath = distantInteraction) ?: return false
                if (dst == pawn.tile) {
                    return false
                }
            }
        } else {
            /**
             * Choose what destination we want our path finder to use. This varies
             * when trying to interact by being next to the [target] or if it can
             * be done in a bigger radius.
             *
             * When it can be done in a bigger radius, we just need to normal
             * target tile. Otherwise, if the interaction must be on the borders
             * of the target, we want to find the closest border tile that isn't
             * clipped.
             */
            val pathEndTile = if (distantInteraction) end else border.sortedBy { tile -> tile.getDistance(pawn.tile) }.firstOrNull { tile -> world.collision.raycast(pawn.tile, tile, projectile = false) } ?: end

            /**
             * Get the shortest path using our path-finding strategy...
             */
            val path = pawn.createPathingStrategy().getPath(start, pathEndTile, pawn.getType())

            var tail: Tile
            var lastTile: Tile? = null

            /**
             * Check to see if any of the tiles in our path are valid tiles
             * to interact with [target] from. If so we set that as our destination.
             */
            while (true) {
                tail = path.poll() ?: break

                /**
                 * Check to see if the tail is a valid tile for interaction.
                 * This varies depending on if the [interactionRange] is > 1
                 * (is projectile) as non-projectile interactions must be in
                 * a border tile for interaction, while projectiles just need
                 * to be within the [interactionRange].
                 */
                valid = if (!distantInteraction) tail in border else !overlap(tail, 0, target.tile, targetSize) && tail.isWithinRadius(end, range)

                if (valid && world.collision.raycast(tail, end, projectile = projectile)) {

                    /**
                     * If the last tile is within a 1 tile radius of the end
                     * target tile, we check to make sure the tail isn't diagonal
                     * in relation to the end tile.
                     *
                     * This is to stop diagonal interactions with targets that
                     * have a size of 1.
                     */
                    if (end.isWithinRadius(tail, 1) && Direction.between(end, tail).isDiagonal()) {
                        val tile = border.sortedBy { tile -> tile.getDistance(start) }.firstOrNull { world.collision.raycast(it, end, projectile = false) }
                                ?: return false
                        dst = pawn.walkTo(tile.x, tile.z, MovementQueue.StepType.NORMAL, projectilePath = false) ?: return false
                        break
                    }

                    if (pawn.tile.sameAs(tail)) {
                        return true
                    }

                    dst = pawn.walkTo(tail.x, tail.z, MovementQueue.StepType.NORMAL, projectilePath = false) ?: continue
                    break
                }
                lastTile = tail
            }
            /**
             * If there's no valid interaction tile in our path, we still want
             * the player to walk to the end of the path before letting them
             * know "they can't reach that" - this is how it is on 07.
             */
            if (dst == null && lastTile != null) {
                dst = pawn.walkTo(lastTile.x, lastTile.z, MovementQueue.StepType.NORMAL, projectilePath = false) ?: return false
            }
            if (dst == null) {
                return false
            }
        }

        /**
         * If the [pawn] has not arrived at our destination yet, we want to wait
         * another cycle and rerun this method to accommodate for any possible changes.
         *
         * Such changes can include:
         * - [target] moved from their last tile.
         * - [pawn] has moved around a wall, which now means the closest tile to [target]
         *      has changed.
         */
        if (!pawn.tile.sameAs(dst)) {
            it.wait(1)
            return walkTo(it, pawn, target, interactionRange)
        }

        /**
         * If the [pawn] has arrived at their destination, check to see if they
         * can successfully interact with the [target] and return the result.
         */
        start = pawn.calculateCentreTile()
        end = target.calculateCentreTile()

        valid = if (!distantInteraction) start in border else start.isWithinRadius(end, range)

        return valid && world.collision.raycast(start, end, projectile = projectile)
    }

    /**
     * Checks to see if two AABB (axis-aligned bounding box) overlap.
     */
    private fun overlap(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean {
        val a = Pair(tile1, tile1.transform(size1, size1))
        val b = Pair(tile2, tile2.transform(size2, size2))

        if (a.first.x > b.second.x || b.first.x > a.second.x) {
            return false
        }

        if (a.first.z > b.second.z || b.first.z > a.second.z) {
            return false
        }

        return true
    }

    /**
     * Get the border tiles around a [target] pawn.
     */
    private fun getBorderTiles(target: Pawn): List<Tile> {
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

        return tiles
    }
}