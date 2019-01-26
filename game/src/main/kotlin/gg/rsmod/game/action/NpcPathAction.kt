package gg.rsmod.game.action

import gg.rsmod.game.message.impl.SetMinimapMarkerMessage
import gg.rsmod.game.model.FROZEN_TIMER
import gg.rsmod.game.model.INTERACTING_NPC_ATTR
import gg.rsmod.game.model.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.Tile
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
        return false
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

        val size = target.getSize()

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