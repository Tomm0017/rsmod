package gg.rsmod.game.sync.task

import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.sync.SynchronizationTask

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcPreSynchronizationTask(val npc: Npc) : SynchronizationTask {

    override fun run() {
        npc.movementQueue.cycle()
    }
}