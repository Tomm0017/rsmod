package gg.rsmod.game.model.coroutine.suspendable.step

import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 * Represents a continuation that can be suspended and stepped over in the future depending on a condition.
 *
 * @author Curtis Woodard <Nbness2@gmail.com>
 */
interface SuspendableStep {

    /** @property [condition] Whether or not to resume this step. */
    val condition: () -> Boolean

    /** @property [continuation] The [Continuation] to step over. */
    val continuation: Continuation<Unit>

    /** Whether or not to resume stepping over [continuation] */
    fun canResume() = condition()

    /** Step over the next step of [continuation] */
    fun resume() = continuation.resume(Unit)
}
