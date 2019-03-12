package gg.rsmod.game.model.queue

import gg.rsmod.game.model.entity.Player
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import java.util.*
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume

/**
 * A system responsible for the the coroutine logic.
 *
 * @param headPriority
 * If true, the last inserted [QueueTask] takes priority and trailing [QueueTask]s are not
 * taken into account until said task is finished.
 * If false, all [QueueTask]s in [queue] will [QueueTask.cycle].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class QueueTaskSystem(private val headPriority: Boolean) {

    private val queue: LinkedList<QueueTask> = LinkedList()

    val size: Int get() = queue.size

    private fun hasMenuOpen(player: Player): Boolean = player.world.plugins.isMenuOpened(player)

    fun queue(ctx: Any, dispatcher: CoroutineDispatcher, priority: TaskPriority, block: suspend QueueTask.(CoroutineScope) -> Unit) {
        val task = QueueTask(ctx, priority)
        val suspendBlock = suspend { block(task, CoroutineScope(dispatcher)) }

        task.coroutine = suspendBlock.createCoroutine(completion = task)

        if (priority == TaskPriority.STRONG) {
            terminateTasks()
        }

        queue.addFirst(task)
    }

    fun cycle() {
        if (headPriority) {
            while (true) {
                val task = queue.peekFirst() ?: break

                /**
                 * @see TaskPriority.STANDARD
                 */
                if (task.priority == TaskPriority.STANDARD && task.ctx is Player && hasMenuOpen(task.ctx)) {
                    break
                }

                if (!task.invoked) {
                    task.invoked = true
                    task.coroutine.resume(Unit)
                }

                task.cycle()

                if (!task.suspended()) {
                    /**
                     * Task is no longer in a suspended state, which means its job is
                     * complete.
                     */
                    queue.remove(task)
                    /**
                     * Since this plugin is complete, let's handle any upcoming
                     * plugin now instead of waiting until next cycle.
                     */
                    continue
                }
                break
            }
        } else {
            val iterator = queue.iterator()
            while (iterator.hasNext()) {
                val task = iterator.next()

                if (!task.invoked) {
                    task.invoked = true
                    task.coroutine.resume(Unit)
                }

                task.cycle()

                if (!task.suspended()) {
                    /**
                     * Plugin is no longer in a suspended state, which means its job is
                     * complete.
                     */
                    iterator.remove()
                }
            }
        }
    }

    /**
     * In-game events sometimes must return a value to a plugin. An example are
     * dialogs which must return values such as input, button click, etc.
     *
     * @param value
     * The return value that the plugin has asked for.
     */
    fun submitReturnValue(value: Any) {
        val task = queue.peek() ?: return // Shouldn't call this method without a queued task.
        task.requestReturnValue = value
    }

    fun terminateTasks() {
        queue.forEach { it.terminate() }
        queue.clear()
    }
}
