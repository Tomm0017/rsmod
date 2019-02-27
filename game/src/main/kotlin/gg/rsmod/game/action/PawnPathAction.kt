package gg.rsmod.game.action

import gg.rsmod.game.message.impl.SetMapFlagMessage
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.attr.FACING_PAWN_ATTR
import gg.rsmod.game.model.attr.INTERACTING_NPC_ATTR
import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.attr.NPC_FACING_US_ATTR
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.timer.FROZEN_TIMER
import gg.rsmod.game.model.timer.RESET_PAWN_FACING_TIMER
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.util.AabbUtil
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PawnPathAction {

    val walkPlugin: Plugin.() -> Unit = {
        val pawn = ctx as Pawn
        val world = pawn.world
        val npc = pawn.attr[INTERACTING_NPC_ATTR]!!.get()!!
        val opt = pawn.attr[INTERACTING_OPT_ATTR]!!
        val npcId = if (pawn is Player) npc.getTransform(pawn) else npc.id
        val lineOfSightRange = world.plugins.getNpcInteractionDistance(npcId)

        suspendable {
            pawn.facePawn(npc)

            val pathFound = walkTo(it, pawn, npc, interactionRange = lineOfSightRange ?: 1, lineOfSight = lineOfSightRange != null)

            if (!pathFound) {
                pawn.movementQueue.clear()
                if (pawn is Player) {
                    if (!pawn.timers.has(FROZEN_TIMER)) {
                        pawn.message(Entity.YOU_CANT_REACH_THAT)
                    } else {
                        pawn.message(Entity.MAGIC_STOPS_YOU_FROM_MOVING)
                    }
                    pawn.write(SetMapFlagMessage(255, 255))
                }
                pawn.facePawn(null)
                return@suspendable
            }

            pawn.stopMovement()

            if (pawn is Player) {
                /**
                 * On 07, only one npc can be facing the player at a time,
                 * so if the last pawn that faced the player is still facing
                 * them, then we reset their face target.
                 */
                pawn.attr[NPC_FACING_US_ATTR]?.get()?.let { other ->
                    if (other.attr[FACING_PAWN_ATTR]?.get() == pawn) {
                        other.facePawn(null)
                        other.timers.remove(RESET_PAWN_FACING_TIMER)
                    }
                }
                pawn.attr[NPC_FACING_US_ATTR] = WeakReference(npc)

                /**
                 * Stop the npc from walking while the player talks to it
                 * for [Npc.RESET_PAWN_FACE_DELAY] cycles.
                 */
                npc.stopMovement()
                if (npc.attr[FACING_PAWN_ATTR]?.get() != pawn) {
                    npc.facePawn(pawn)
                    npc.timers[RESET_PAWN_FACING_TIMER] = Npc.RESET_PAWN_FACE_DELAY
                }
                pawn.facePawn(null)
                pawn.faceTile(npc.tile)

                val handled = world.plugins.executeNpc(pawn, npcId, opt)
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

            if (!projectile) {
                return if (!lineOfSight) {
                    bordering(sourceTile, sourceSize, targetTile, interactionRange)
                } else {
                    overlap(sourceTile, sourceSize, targetTile, interactionRange) && (interactionRange == 0 || !sourceTile.sameAs(targetTile))
                            && pawn.world.collision.raycast(sourceTile, targetTile, lineOfSight)
                }
            }
        }

        val builder = PathRequest.Builder()
                .setPoints(sourceTile, targetTile)
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

    private fun diagonal(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean = AabbUtil.areDiagonal(tile1.x, tile1.z, size1, size1, tile2.x, tile2.z, size2, size2)
}