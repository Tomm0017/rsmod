package gg.rsmod.game.model.coroutine.suspendable.condition.impl

import gg.rsmod.game.model.coroutine.suspendable.condition.SuspendableCondition
import java.util.concurrent.atomic.AtomicInteger

/**
 * A [SuspendableCondition] that waits for the given amount of cycles before
 * permitting the coroutine to continue its logic.
 *
 * @param [cycles]
 * The amount of game cycles that must pass before the coroutine can continue.
 *
 * @author Curtis Woodard <Nbness2@gmail.com>
 */
class SynchronousCondition(cycles: Int) : SuspendableCondition {

    private val cyclesLeft = AtomicInteger(cycles)

    override fun canResume(): Boolean = cyclesLeft.decrementAndGet() <= 0

    override fun toString(): String = "SynchronousCondition($cyclesLeft)"
}
