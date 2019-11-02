package gg.rsmod.game.model.coroutine.suspendable.step.impl

import gg.rsmod.game.model.coroutine.suspendable.condition.SuspendableCondition
import gg.rsmod.game.model.coroutine.suspendable.step.SuspendableStep
import kotlin.coroutines.Continuation

/**
 * A Sample implementation of [SuspendableStep]
 *
 * @param [condition] A [SuspendableCondition] (possible because [SuspendableCondition] extends `() -> Unit`) that
 *      decides whether or not to step on to the next step of this [SuspendableStep]
 *
 * @param [continuation] The [Continuation] that you are stepping over.
 *
 * @author Curtis Woodard <Nbness2@gmail.com>
 */
class SuspendableStepImpl(
        override val condition: SuspendableCondition,
        override val continuation: Continuation<Unit>
): SuspendableStep