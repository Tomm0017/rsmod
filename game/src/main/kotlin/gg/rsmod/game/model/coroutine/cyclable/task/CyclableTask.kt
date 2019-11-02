package gg.rsmod.game.model.coroutine.cyclable.task

import gg.rsmod.game.model.coroutine.suspendable.step.SuspendableStep
import gg.rsmod.game.model.coroutine.suspendable.condition.SuspendableCondition
import gg.rsmod.game.model.queue.TaskPriority
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Represents a [Continuation] that can be stepped over in "cycles" according to a scheduled executor.
 * This can potentially never finish it's logic.
 *
 * @author Curtis Woodard <Nbness2@gmail.com
 */
interface CyclableTask: Continuation<Unit> {

    /**
     * @property [ctx]
     * An object that should be casted to a certain type depending on where it is used.
     */
    val ctx: Any

    /**
     * @property [priority]
     * The execution priority of this task.
     */
    val priority: TaskPriority

    /**
     * @property [context]
     * The [CoroutineContext] implementation for this task.
     */
    override val context: CoroutineContext
        get() = EmptyCoroutineContext


    /**
     * @property [terminateAction]
     * Represents an action that should be executed only if this action was terminated via [terminate]
     */
    val terminateAction: Function<Unit>

    /**
     * @property [onResultException]
     * Represents an action that should be executed only when [resumeWith] `result` paramteter is an exception or `null`
     */
    var onResultException: (Throwable) -> Unit

    /**
     * @property [coroutine]
     * The [Continuation] that will be cycled over
     */
    var coroutine: Continuation<Unit>

    /**
     * @property [invoked]
     * Whether or not this task's logic has been invoked during the cycle.
     */
    var invoked: Boolean

    /**
     * @property [nextStep]
     * The next [SuspendableStep] that will be executed once it's [SuspendableCondition] returns `true`
     */
    var nextStep: SuspendableStep?

    /**
     * @property [currentCycle]
     * The cycle of the current task
     */
    val currentCycle: AtomicInteger

    /**
     * @property [lastcycle]
     * The last cycle used for cycle-based logic in the current task
     */
    val lastCycle: AtomicInteger

    /**
     * @property [requestReturnValue]
     * Value requested by a task e.g. an input for a dialogue.
     */
    var requestReturnValue: Any?

    /**
     * Terminate the execution of the rest of this task.
     * Call [terminateAction] if applicable.
     */
    fun terminate()

    /**
     * If the task has been suspended.
     */
    fun suspended(): Boolean = nextStep is SuspendableStep

    /**
     * Synchronizes the logic with the rest of the game thread.
     */
    fun cycle() {
        val next = nextStep ?: return

        if (next.canResume()) {
            next.resume()
        }

        currentCycle.incrementAndGet()
    }

    /**
     * Resumes [coroutine] with [result]
     */
    override fun resumeWith(result: Result<Unit>) {
        nextStep = null
        onResultException(result.exceptionOrNull() ?: return)
    }
}