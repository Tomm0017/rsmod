package gg.rsmod.game.model.queue

import gg.rsmod.game.plugin.Plugin
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
 * If false, all [QueueTask]s in [queue] have their plugin invoke [Plugin.cycle]
 * every [cycle].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class QueueTaskSystem(private val headPriority: Boolean) {

    /**
     * TODO:
     *
     * Current situation:
     * Our "walk-to" for pawn and object targets are kept alive until we arrive
     * to our destination. This is not the case on 07 as seen by the following
     * situation:
     *
     * Walk to an npc -> open xp drop setup -> npc moves from original location
     *
     * In the above case, you will walk to the original destination and wait until
     * your xp drop setup window has been closed - once it's closed, if the npc is not
     * within range, you will walk towards it again.
     *
     * What this tells us is that the walk-to is not a queue, but the execution
     * of the "npc action" is.
     */

    private val queue: LinkedList<QueueTask> = LinkedList()

    val size: Int get() = queue.size

    fun queue(ctx: Any, dispatcher: CoroutineDispatcher, priority: TaskPriority, block: suspend Plugin.(CoroutineScope) -> Unit) {
        val plugin = Plugin(ctx)
        val suspendBlock = suspend { block(plugin, CoroutineScope(dispatcher)) }
        val task = QueueTask(priority, plugin, suspendBlock.createCoroutine(completion = plugin))

        if (priority == TaskPriority.STRONG) {
            terminateTasks()
        }

        if (queue.isEmpty()) {
            task.invoked = true
            task.continuation.resume(Unit)
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

    fun terminateTasks() {
        queue.forEach { it.plugin.terminate() }
        queue.clear()
    }
}
