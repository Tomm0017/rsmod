package gg.rsmod.game.model.queue.impl

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTaskSet
import gg.rsmod.game.model.queue.TaskPriority
import kotlin.coroutines.resume

/**
 * A [QueueTaskSet] implementation for [gg.rsmod.game.model.entity.Pawn]s.
 * Each [gg.rsmod.game.model.queue.QueueTask] is handled one at a time.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PawnQueueTaskSet : QueueTaskSet() {

    override fun cycle() {
        while (true) {
            val task = queue.peekFirst() ?: break

            if (task.priority == TaskPriority.STANDARD && task.ctx is Player && task.ctx.hasMenuOpen()) {
                break
            }

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
                queue.remove(task)
                /*
                 * Since this task is complete, let's handle any upcoming
                 * task now instead of waiting until next cycle.
                 */
                continue
            }
            break
        }
    }

    private fun Player.hasMenuOpen(): Boolean = world.plugins.isMenuOpened(this)
}