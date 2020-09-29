package gg.rsmod.game.model.coroutine.suspendable.step

import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 * Represents a continuation that can be suspended adn stepped over in the future depending on a condition.
 *
 * @author Curtis Woodard <Nbness2@gmail.com>
 */
interface SuspendableStep: Continuation<Unit> {
    /** @property [predicate] The predicate lambda that decides whether or not to resume this [SuspendableStep] */
    val predicate: () -> Boolean

    /** @property [continuation] The [Continuation] to step over */
    val continuation: Continuation<Unit>

    /**
     * Decides whether or not to resume [continuation]
     * @return [Boolean]
     */
    fun canResume(): Boolean = predicate()

    /** Resumes the next step of [continuation] */
    fun resume() = continuation.resume(Unit)
}