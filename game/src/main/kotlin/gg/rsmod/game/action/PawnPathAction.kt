package gg.rsmod.game.action

import gg.rsmod.game.message.impl.SetMinimapMarkerMessage
import gg.rsmod.game.model.*
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.plugin.Plugin

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PawnPathAction {

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
        val world = pawn.world
        val sourceSize = pawn.getSize()
        val targetSize = target.getSize()
        val projectile = interactionRange > 2
        val targetTile = target.tile
        val frozen = pawn.timers.has(FROZEN_TIMER)

        if (frozen && (!pawn.tile.isWithinRadius(target.calculateCentreTile(), interactionRange) || overlap(pawn.tile, sourceSize, target.tile, targetSize))) {
            return false
        }

        val request = PathRequest.Builder()
                .setPoints(pawn.tile, target.tile)
                .setSourceSize(sourceSize, sourceSize)
                .setTargetSize(targetSize, targetSize)
                .setProjectilePath(projectile)
                .setTouchRadius(interactionRange)
                .clipBorderTiles(world.collision)
                .clipPathNodes(world.collision, tile = true, face = true)
                .clipDiagonalTiles()
                .clipOverlapTiles()
                .build()

        val route = pawn.createPathingStrategy().calculateRoute(request)
        pawn.walkPath(route.path, MovementQueue.StepType.NORMAL)

        while (!pawn.tile.sameAs(route.tail)) {
            if (!targetTile.sameAs(target.tile)) {
                return walkTo(it, pawn, target, interactionRange)
            }
            it.wait(1)
        }

        return route.success
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
}