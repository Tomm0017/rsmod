package gg.rsmod.game.model.coroutine.suspendable.condition

/**
 * Represents a condition to be used for suspending something
 *
 * @author Curtis Woodard <Nbness2@gmail.com>
 */
interface SuspendableCondition: () -> Boolean {
    /** [canResume] Decides whether or not to continue. */
    fun canResume(): Boolean
    override fun invoke(): Boolean = canResume()
}