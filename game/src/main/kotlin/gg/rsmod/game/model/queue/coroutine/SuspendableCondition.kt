package gg.rsmod.game.model.queue.coroutine

import com.google.common.base.MoreObjects
import gg.rsmod.game.model.Tile
import java.util.concurrent.atomic.AtomicInteger

/**
 * A condition that must be met for a suspended coroutine to continue.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class SuspendableCondition {
    /**
     * Whether or not the coroutine can continue its logic.
     */
    abstract fun resume(): Boolean
}

/**
 * A [SuspendableCondition] that waits for the given amount of cycles before
 * permitting the coroutine to continue its logic.
 *
 * @param cycles
 * The amount of game cycles that must pass before the coroutine can continue.
 */
class WaitCondition(cycles: Int) : SuspendableCondition() {

    private val cyclesLeft = AtomicInteger(cycles)

    override fun resume(): Boolean = cyclesLeft.decrementAndGet() <= 0

    override fun toString(): String = MoreObjects.toStringHelper(this).add("cycles", cyclesLeft).toString()
}

/**
 * A [SuspendableCondition] that waits for [src] to possess the exact same
 * coordinates as [dst] before permitting the coroutine to continue its logic.
 *
 * Note that the [src] and [dst] can't be the same coordinates if their height
 * does not match as well as their x and z coordinates.
 *
 * @param src
 * The tile that must reach [dst] before the condition returns true.
 *
 * @param dst
 * The tile that must be reached by [dst].
 */
class TileCondition(private val src: Tile, private val dst: Tile) : SuspendableCondition() {

    override fun resume(): Boolean = src.sameAs(dst)

    override fun toString(): String = MoreObjects.toStringHelper(this).add("src", src).add("dst", dst).toString()
}

/**
 * A [SuspendableCondition] that waits for [predicate] to return true before
 * permitting the coroutine to continue its logic.
 */
class PredicateCondition(private val predicate: () -> Boolean) : SuspendableCondition() {

    override fun resume(): Boolean = predicate.invoke()
}