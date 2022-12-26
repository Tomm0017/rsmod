package gg.rsmod.game.model.coroutine.suspendable.condition

/**
 * Represents a condition that can be invoked.
 * Meant to be used as a condition for suspending something.
 *
 * @author Curtis Woodard <Nbness2@gmail.com>
 */
interface SuspendableCondition: () -> Boolean {
    /**
     * Should be called to decide whether or not to continue the thing this is inside.
     *
     * @return [Boolean]
     */
    fun canResume(): Boolean

    /**
     * Emulates the boolean predicate lambda functionality that this "implements"
     *
     * @return [Boolean]
     */
    override fun invoke(): Boolean = canResume()
}
