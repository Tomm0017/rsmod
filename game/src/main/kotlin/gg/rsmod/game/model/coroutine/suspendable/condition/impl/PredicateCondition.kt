package gg.rsmod.game.model.coroutine.suspendable.condition.impl

import gg.rsmod.game.model.coroutine.suspendable.condition.SuspendableCondition

/**
 * A [SuspendableCondition] that waits for [predicate] to return true before
 * permitting the coroutine to continue its logic.
 *
 * @param [predicate]
 *   The function used to determine the result of [canResume]
 *
 * @author Curtis Woodard <Nbness2@gmail.com>
 */
class PredicateCondition(private inline val predicate: () -> Boolean) : SuspendableCondition {

    override fun canResume(): Boolean = predicate.invoke()
}
