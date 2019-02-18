package gg.rsmod.game.action

import gg.rsmod.game.message.impl.SetMapFlagMessage
import gg.rsmod.game.model.*
import gg.rsmod.game.model.attr.INTERACTING_NPC_ATTR
import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.timer.FROZEN_TIMER
import gg.rsmod.game.model.timer.RESET_PAWN_FACING_TIMER
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.util.AabbUtil

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PawnPathAction {

    val walkPlugin: (Plugin) -> Unit = {
        val pawn = it.ctx as Pawn
        val world = pawn.world
        val npc = pawn.attr[INTERACTING_NPC_ATTR]!!.get()!!
        val opt = pawn.attr[INTERACTING_OPT_ATTR]!!
        val lineOfSightRange = world.plugins.getNpcInteractionDistance(npc.id)

        it.suspendable {
            pawn.facePawn(npc)

            val pathFound = walkTo(it, pawn, npc, interactionRange = lineOfSightRange ?: 1, lineOfSight = lineOfSightRange != null)

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
                /**
                 * Stop the npc from walking while the player talks to it
                 * for [Npc.RESET_PAWN_FACE_DELAY] cycles.
                 */
                npc.stopMovement()
                npc.facePawn(pawn)
                npc.timers[RESET_PAWN_FACING_TIMER] = Npc.RESET_PAWN_FACE_DELAY

                val handled = world.plugins.executeNpc(pawn, npc.id, opt)
                if (!handled) {
                    pawn.message(Entity.NOTHING_INTERESTING_HAPPENS)
                }
            }
        }
    }

    suspend fun walkTo(it: Plugin, pawn: Pawn, target: Pawn, interactionRange: Int, lineOfSight: Boolean): Boolean {
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

            return if (!lineOfSight) {
                bordering(sourceTile, sourceSize, targetTile, interactionRange)
            } else {
                overlap(sourceTile, sourceSize, targetTile, interactionRange) && (interactionRange == 0 || !sourceTile.sameAs(targetTile))
                        && pawn.world.collision.raycast(sourceTile, targetTile, lineOfSight)
            }
        }

        val builder = PathRequest.Builder()
                .setPoints(pawn.tile, target.tile)
                .setSourceSize(sourceSize, sourceSize)
                .setTargetSize(targetSize, targetSize)
                .setProjectilePath(lineOfSight || projectile)
                .setTouchRadius(interactionRange)
                .clipPathNodes(node = true, link = true)

        if (!lineOfSight && !projectile) {
            builder.clipOverlapTiles().clipDiagonalTiles()
        }

        val route = pawn.createPathFindingStrategy().calculateRoute(builder.build())
        pawn.walkPath(route.path)

        while (!pawn.tile.sameAs(route.tail)) {
            if (!targetTile.sameAs(target.tile)) {
                return walkTo(it, pawn, target, interactionRange, lineOfSight)
            }
            it.wait(1)
        }

        return route.success
    }

    private fun overlap(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean = AabbUtil.areOverlapping(tile1.x, tile1.z, size1, size1, tile2.x, tile2.z, size2, size2)

    private fun bordering(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean = AabbUtil.areBordering(tile1.x, tile1.z, size1, size1, tile2.x, tile2.z, size2, size2)
}