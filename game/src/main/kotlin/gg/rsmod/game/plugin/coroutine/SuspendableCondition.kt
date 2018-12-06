package gg.rsmod.game.plugin.coroutine

import com.google.common.base.MoreObjects
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Tom <rspsmods@gmail.com>
 */
abstract class SuspendableCondition {
    abstract fun resume(): Boolean
}

class WaitCondition(cycles: Int) : SuspendableCondition() {

    private val cyclesLeft = AtomicInteger(cycles)

    override fun resume(): Boolean = cyclesLeft.decrementAndGet() <= 0

    override fun toString(): String = MoreObjects.toStringHelper(this).add("cycles", cyclesLeft).toString()
}

class PredicateCondition(private val predicate: () -> Boolean) : SuspendableCondition() {

    override fun resume(): Boolean = predicate.invoke()
}