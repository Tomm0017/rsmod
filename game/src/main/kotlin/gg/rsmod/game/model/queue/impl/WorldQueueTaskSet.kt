package gg.rsmod.game.model.queue.impl

import gg.rsmod.game.model.queue.QueueTaskSet
import kotlin.coroutines.resume

/**
 * @author Tom <rspsmods@gmail.com>
 */
class WorldQueueTaskSet : QueueTaskSet() {

    override fun cycle() {
        val iterator = queue.iterator()
        while (iterator.hasNext()) {
            val task = iterator.next()

            if (!task.invoked) {
                task.invoked = true
                task.coroutine.resume(Unit)
            }

            task.cycle()

            if (!task.suspended()) {
                /*
                 * Task is no longer in a suspended state, which means its job is
                 * complete.
                 */
                iterator.remove()
            }
        }
    }
}