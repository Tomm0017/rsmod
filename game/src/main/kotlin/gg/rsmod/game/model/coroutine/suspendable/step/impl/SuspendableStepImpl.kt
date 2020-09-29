package gg.rsmod.game.model.coroutine.suspendable.step.impl

import gg.rsmod.game.model.coroutine.suspendable.condition.SuspendableCondition
import gg.rsmod.game.model.coroutine.suspendable.step.SuspendableStep
import kotlin.coroutines.Continuation

/**
 * A simple sample implementation of [SuspendableStep]
 *
 * @property [predicate] A [SuspendableCondition] (which extends `() -> Unit`) that decides whether or not to step
 *  on to the next step of this [SuspendableStep]
 * @property [continuation] The [Continuation] that is being stepped over
 *  and the [Continuation] to which [SuspendableStepImpl]'s [Continuation] functionality is delegated to.
 *
 *  @author Curtis Woodard <Nbness2@gmail.com>
 */
class SuspendableStepImpl(
        override val predicate: SuspendableCondition,
        override val continuation: Continuation<Unit>
): SuspendableStep, Continuation<Unit> by continuation
