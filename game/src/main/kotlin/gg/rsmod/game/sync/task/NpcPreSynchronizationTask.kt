package gg.rsmod.game.sync.task

import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.sync.SynchronizationTask

/**
 * @author Tom <rspsmods@gmail.com>
 */
object NpcPreSynchronizationTask : SynchronizationTask<Npc> {

    override fun run(pawn: Npc) {
        pawn.movementQueue.cycle()
    }
}