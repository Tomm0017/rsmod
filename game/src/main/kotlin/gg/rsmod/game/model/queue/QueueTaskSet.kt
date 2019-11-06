package gg.rsmod.game.model.queue

import gg.rsmod.game.model.coroutine.cyclable.set.CyclableTaskSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import java.util.*
import kotlin.coroutines.createCoroutine

abstract class QueueTaskSet: CyclableTaskSet<QueueTask> {

    override val queue: LinkedList<QueueTask> = LinkedList()

    override fun queue(
            ctx: Any,
            dispatcher: CoroutineDispatcher,
            priority: TaskPriority,
            block: suspend QueueTask.(CoroutineScope) -> Unit
    ) {
        val task = QueueTask(ctx, priority)
        val suspendBlock = suspend { block(task, CoroutineScope(dispatcher)) }

        task.coroutine = suspendBlock.createCoroutine(completion = task)

        if (priority == TaskPriority.STRONG) {
            terminateTasks()
        }

        queue.addFirst(task)
    }
}
