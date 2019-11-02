package gg.rsmod.game.model.coroutine.cyclable.set

import gg.rsmod.game.model.coroutine.cyclable.task.CyclableTask
import gg.rsmod.game.model.queue.TaskPriority
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*

/**
 * The data structure created to queue up [T], cycle over them and get rid of them.
 *
 * @author Curtis Woodard <Nbness2@gmail.com>
 */
interface CyclableTaskSet<T: CyclableTask> {
    /**
     * @property [queue]
     *  Holds all of the [T] that will be cycled over when [cycle] is called
     */
    val queue: LinkedList<T>

    /**
     * @property [size]
     *  The size of [queue]
     */
    val size: Int get() = queue.size

    /**
     * The logic that cycles over each [T] in [Queue]
     */
    fun cycle()

    /**
     * The function that lets you queue up logic to be cycled over
     *
     * @param [ctx] The object to use in this logic
     * @param [dispatcher] The [CoroutineDispatcher] that dispatches this logic
     * @param [priority] The priority given to this logic
     * @param [block] The logic to be run
     */
    fun queue(
            ctx: Any,
            dispatcher: CoroutineDispatcher = Dispatchers.Default,
            priority: TaskPriority = TaskPriority.STANDARD,
            block: suspend T.(CoroutineScope) -> Unit
    )

    /**
     * Submits a return value for the first [T] in [queue]
     *
     * @param [value] The value to submit to the first [T] in [queue]
     */
    fun submitReturnValue(value: Any) {
        val task = queue.peek() ?: return
        task.requestReturnValue = value
    }

    /**
     * Terminates all [T] in [queue]
     */
    fun terminateTasks() {
        queue.forEach(CyclableTask::terminate)
        queue.clear()
    }
}