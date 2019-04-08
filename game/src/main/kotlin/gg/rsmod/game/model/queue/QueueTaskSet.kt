package gg.rsmod.game.model.queue

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import java.util.*
import kotlin.coroutines.createCoroutine

/**
 * A system responsible for task coroutine logic.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class QueueTaskSet {

    protected val queue: LinkedList<QueueTask> = LinkedList()

    val size: Int get() = queue.size

    abstract fun cycle()

    fun queue(ctx: Any, dispatcher: CoroutineDispatcher, priority: TaskPriority, block: suspend QueueTask.(CoroutineScope) -> Unit) {
        val task = QueueTask(ctx, priority)
        val suspendBlock = suspend { block(task, CoroutineScope(dispatcher)) }

        task.coroutine = suspendBlock.createCoroutine(completion = task)

        if (priority == TaskPriority.STRONG) {
            terminateTasks()
        }

        queue.addFirst(task)
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

    /**
     * Remove all [QueueTask] from our [queue], invoking each task's [QueueTask.terminate]
     * before-hand.
     */
    fun terminateTasks() {
        queue.forEach { it.terminate() }
        queue.clear()
    }
}
