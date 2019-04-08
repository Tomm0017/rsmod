package gg.rsmod.game.sync.task

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.sync.SynchronizationTask

/**
 * @author Tom <rspsmods@gmail.com>
 */
object NpcPostSynchronizationTask : SynchronizationTask<Npc> {

    override fun run(pawn: Npc) {
        val moved = pawn.lastTile == null || pawn.lastTile?.sameAs(pawn.tile) != true
        if (moved) {
            pawn.lastTile = Tile(pawn.tile)
        }
        pawn.teleport = false
        pawn.steps = null
        pawn.blockBuffer.clean()
    }
}