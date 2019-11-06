package gg.rsmod.game.model.queue

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.coroutine.cyclable.task.CyclableTask
import gg.rsmod.game.model.coroutine.suspendable.condition.impl.*
import gg.rsmod.game.model.coroutine.suspendable.step.SuspendableStep
import gg.rsmod.game.model.coroutine.suspendable.step.impl.*
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import mu.KLogging
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.*

/**
 * Represents a task that can be paused, or suspended, and resumed at any point
 * in the future.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class QueueTask(override val ctx: Any, override val priority: TaskPriority) : CyclableTask {
    override var onResultException: (Throwable) -> Unit = { }

    override val currentCycle: AtomicInteger = AtomicInteger(0)

    override val lastCycle: AtomicInteger = AtomicInteger(0)

    override lateinit var coroutine: Continuation<Unit>

    override var invoked = false

    override var requestReturnValue: Any? = null

    override var terminateAction: (QueueTask).() -> Unit = { }

    override var nextStep: SuspendableStep? = null

    override val context: CoroutineContext = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        nextStep = null
        result.exceptionOrNull()?.let { e -> logger.error("Error with plugin!", e) }
    }

    override fun cycle() {
        val next = nextStep ?: return

        if (next.condition()) {
            next.continuation.resume(Unit)
            requestReturnValue = null
        }
    }

    override fun terminate() {
        nextStep = null
        requestReturnValue = null
        terminateAction(this)
    }

    override fun suspended(): Boolean = nextStep != null

    /**
     * Wait for the specified amount of game cycles [cycles] before
     * continuing the logic associated with this task.
     */
    suspend fun wait(cycles: Int): Unit = suspendCoroutine {
        check(cycles > 0) { "Wait cycles must be greater than 0." }
        check(cycles > lastCycle.get()) { "Wait cycle must be after the previous cycle ${lastCycle.get()}" }
        lastCycle.set(currentCycle.get() + cycles)
        nextStep = SuspendableStepImpl(SynchronousCondition(cycles), it)
    }

    /**
     * Wait for [currentCycle] to equal [cycle] before continuing logic associated with this task.
     */
    suspend fun onCycle(cycle: Int): Unit = suspendCoroutine {
        check(cycle > 0) { "Execution cycle must be greater than 0."}
        check(cycle > lastCycle.get()) { "Execution cycle must be after the previous cycle ${lastCycle.get()}"}
        lastCycle.set(cycle)
        nextStep = SuspendableStepImpl(AsynchronousCondition(cycle, currentCycle), it)
    }

    /**
     * Wait for [predicate] to return true.
     */
    suspend fun wait(predicate: () -> Boolean): Unit = suspendCoroutine {
        nextStep = SuspendableStepImpl(PredicateCondition { predicate() }, it)
    }

    /**
     * Wait for our [ctx] to reach [tile]. Note that [ctx] MUST be an instance
     * of [Pawn] and that the height of the [tile] and [Pawn.tile] must be equal,
     * as well as the x and z coordinates.
     */
    suspend fun waitTile(tile: Tile): Unit = suspendCoroutine {
        nextStep = SuspendableStepImpl(TileCondition((ctx as Pawn).tile, tile), it)
    }

    /**
     * Wait for our [ctx] as [Player] to close the [interfaceId].
     */
    suspend fun waitInterfaceClose(interfaceId: Int): Unit = suspendCoroutine {
        nextStep = SuspendableStepImpl(PredicateCondition { !(ctx as Player).interfaces.isVisible(interfaceId) }, it)
    }

    /**
     * Wait for <strong>any</strong> return value to be available before
     * continuing.
     */
    suspend fun waitReturnValue(): Unit = suspendCoroutine {
        nextStep = SuspendableStepImpl(PredicateCondition { requestReturnValue != null }, it)
    }

    override fun equals(other: Any?): Boolean {
        val o = other as? QueueTask ?: return false
        return super.equals(o) && o.coroutine == coroutine
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + coroutine.hashCode()
        return result
    }

    class EmptyReturnValue

    companion object : KLogging() {
        val EMPTY_RETURN_VALUE = EmptyReturnValue()
    }
}