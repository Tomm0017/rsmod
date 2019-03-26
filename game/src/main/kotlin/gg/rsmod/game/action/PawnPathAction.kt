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
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.game.model.queue.TaskPriority
import gg.rsmod.game.model.timer.FROZEN_TIMER
import gg.rsmod.game.model.timer.RESET_PAWN_FACING_TIMER
import gg.rsmod.game.model.timer.STUN_TIMER
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

        pawn.queue(TaskPriority.STANDARD) {
            terminateAction = {
                pawn.stopMovement()
                if (pawn is Player) {
                    pawn.write(SetMapFlagMessage(255, 255))
                }
            }

            walk(this, pawn, npc, npcId, opt, lineOfSightRange)
        }
    }

    suspend fun walk(it: QueueTask, pawn: Pawn, npc: Npc, npcId: Int, opt: Int, lineOfSightRange: Int?) {
        val world = pawn.world
        val initialTile = Tile(npc.tile)

        pawn.facePawn(npc)

        val pathFound = walkTo(it, pawn, npc, interactionRange = lineOfSightRange ?: 1, lineOfSight = lineOfSightRange != null)
        if (!pathFound) {
            pawn.movementQueue.clear()
            if (pawn is Player) {
                when {
                    pawn.timers.has(FROZEN_TIMER) -> pawn.message(Entity.MAGIC_STOPS_YOU_FROM_MOVING)
                    pawn.timers.has(STUN_TIMER) -> pawn.message(Entity.YOURE_STUNNED)
                    else -> pawn.message(Entity.YOU_CANT_REACH_THAT)
                }
                pawn.write(SetMapFlagMessage(255, 255))
            }
            pawn.resetFacePawn()
            return
        }

        pawn.stopMovement()

        if (pawn is Player) {
            if (pawn.attr[FACING_PAWN_ATTR]?.get() != npc) {
                return
            }
            /**
             * If the npc has moved from the time this queue was added to
             * when it was actually invoked, we need to walk towards it again.
             */
            if (!npc.tile.sameAs(initialTile)) {
                walk(it, pawn, npc, npcId, opt, lineOfSightRange)
                return
            }
            /**
             * On 07, only one npc can be facing the player at a time,
             * so if the last pawn that faced the player is still facing
             * them, then we reset their face target.
             */
            pawn.attr[NPC_FACING_US_ATTR]?.get()?.let { other ->
                if (other.attr[FACING_PAWN_ATTR]?.get() == pawn) {
                    other.resetFacePawn()
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
            pawn.resetFacePawn()
            pawn.faceTile(npc.tile)

            val handled = world.plugins.executeNpc(pawn, npcId, opt)
            if (!handled) {
                pawn.message(Entity.NOTHING_INTERESTING_HAPPENS)
            }
        }
    }

    suspend fun walkTo(it: QueueTask, pawn: Pawn, target: Pawn, interactionRange: Int, lineOfSight: Boolean): Boolean {
        val sourceSize = pawn.getSize()
        val targetSize = target.getSize()
        val sourceTile = pawn.tile
        val targetTile = target.tile
        val projectile = interactionRange > 2

        val frozen = pawn.timers.has(FROZEN_TIMER)
        val stunned = pawn.timers.has(STUN_TIMER)

        if (pawn.attr[FACING_PAWN_ATTR]?.get() != target) {
            return false
        }

        if (stunned) {
            return false
        }

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

}