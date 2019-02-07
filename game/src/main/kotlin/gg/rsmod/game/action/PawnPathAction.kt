package gg.rsmod.game.action

import gg.rsmod.game.message.impl.SetMapFlagMessage
import gg.rsmod.game.model.*
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.util.AabbUtil

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
                    pawn.write(SetMapFlagMessage(255, 255))
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
        val sourceSize = pawn.getSize()
        val targetSize = target.getSize()
        val sourceTile = pawn.tile
        val targetTile = target.tile
        val projectile = interactionRange > 2

        val frozen = pawn.timers.has(FROZEN_TIMER)

        if (frozen) {
            if (overlap(sourceTile, sourceSize, targetTile, targetSize)) {
                return false
            }
            val sourceCentre = sourceTile.transform(sourceSize / 2, sourceSize / 2)
            val targetCentre = targetTile.transform(targetSize / 2, targetSize / 2)
            if (!sourceCentre.isWithinRadius(targetCentre, interactionRange)) {
                return false
            }
        }

        val request = PathRequest.Builder()
                .setPoints(pawn.tile, target.tile)
                .setSourceSize(sourceSize, sourceSize)
                .setTargetSize(targetSize, targetSize)
                .setProjectilePath(projectile)
                .setTouchRadius(interactionRange)
                .clipPathNodes(node = true, link = true)
                .clipDiagonalTiles()
                .clipOverlapTiles()
                .build()

        val route = pawn.createPathFindingStrategy().calculateRoute(request)
        pawn.walkPath(route.path, MovementQueue.StepType.NORMAL)

        while (!pawn.tile.sameAs(route.tail)) {
            if (!targetTile.sameAs(target.tile)) {
                return walkTo(it, pawn, target, interactionRange)
            }
            it.wait(1)
        }

        return route.success
    }

    private fun overlap(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean = AabbUtil.areOverlapping(tile1.x, tile1.z, size1, size1, tile2.x, tile2.z, size2, size2)
}