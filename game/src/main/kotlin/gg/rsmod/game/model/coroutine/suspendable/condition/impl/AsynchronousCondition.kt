package gg.rsmod.game.model.coroutine.suspendable.condition.impl

import gg.rsmod.game.model.coroutine.suspendable.condition.SuspendableCondition
import java.util.concurrent.atomic.AtomicInteger

/**
 * A [SuspendableCondition] that waits until the given [cycleCounter] is equal to [myCycle] before
 *  permitting the coroutine to continue it's logic.
 *
 *  @param [myCycle]
 *    The cycle on which to permit the coroutine to continue it's logic
 *
 *  @param [cycleCounter]
 *    The counter that decides whether or not the coroutine gets to continue
 *
 *  @author Curtis Woodard <Nbness2@gmail.com>
 */

class AsynchronousCondition(private val myCycle: Int, private val cycleCounter: AtomicInteger): SuspendableCondition {

    override fun canResume(): Boolean = myCycle == cycleCounter.get()

    override fun toString(): String = "AsynchronousCondition(myCycle=$myCycle, currentCycle=$cycleCounter)"
}
