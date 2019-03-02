package gg.rsmod.game.model.queue

import gg.rsmod.game.plugin.Plugin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import java.util.*
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume

/**
 * @author Tom <rspsmods@gmail.com>
 */
class QueueTaskSystem(private val headPriority: Boolean) {

    private val queue: LinkedList<QueueTask> = LinkedList()

    val size: Int get() = queue.size

    fun queue(ctx: Any, dispatcher: CoroutineDispatcher, priority: QueueTaskPriority, block: suspend Plugin.(CoroutineScope) -> Unit) {
        val plugin = Plugin(ctx)
        val suspendBlock = suspend { block(plugin, CoroutineScope(dispatcher)) }
        val task = QueueTask(priority, plugin, suspendBlock.createCoroutine(completion = plugin))

        if (priority == QueueTaskPriority.TERMINATE_PREVIOUS) {
            terminateAll()
        }

        queue.addFirst(task)
    }

    fun cycle() {
        if (headPriority) {
            while (true) {
                val task = queue.peek() ?: break
                val plugin = task.plugin

                if (!task.invoked) {
                    task.invoked = true
                    task.continuation.resume(Unit)
                }

                plugin.cycle()

                if (!plugin.suspended()) {
                    /**
                     * Plugin is no longer in a suspended state, which means its job is
                     * complete.
                     */
                    queue.remove()
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
                val plugin = iterator.next().plugin

                plugin.cycle()

                if (!plugin.suspended()) {
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
        val task = queue.peek()!! // Shouldn't call this method without a queued task.
        val plugin = task.plugin
        plugin.requestReturnValue = value
    }

    /**
     * Terminates all elements in [queue] and invokes [Plugin.terminate]
     * for each [QueueTask]
     */
    fun terminateAll() {
        queue.forEach { it.plugin.terminate() }
        queue.clear()
    }
}
