package gg.rsmod.game.plugin

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.coroutine.SuspendableStep
import gg.rsmod.game.plugin.coroutine.WaitCondition
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.*

/**
 * Represents a plugin that can be executed at any time by a context.
 *
 * @param ctx Can be anything from [Player] to [gg.rsmod.game.model.entity.Pawn].
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Plugin(val ctx: Any?, val dispatcher: CoroutineDispatcher) : Continuation<Unit> {

    var started = false

    /**
     * Can represent an action that should be executed if, and only if, this plugin
     * was interrupted by another action such as walking or a new script being
     * executed by the same [ctx].
     */
    var interruptAction: Function0<Unit>? = null

    private var nextStep: SuspendableStep? = null

    override val context: CoroutineContext = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        nextStep = null
    }

    fun suspendable(block: suspend CoroutineScope.() -> Unit) {
        val coroutine = block.createCoroutine(receiver = CoroutineScope(dispatcher), completion = this)
        coroutine.resume(Unit)
    }

    fun pulse() {
        val next = nextStep ?: return

        if (next.condition.resume()) {
            next.continuation.resume(Unit)
        }
    }

    fun canKill(): Boolean = nextStep == null

    /**
     * Wait for the specified amount of game cycles [cycles] before
     * continuing the logic associated with this plugin.
     */
    suspend fun wait(cycles: Int): Unit = suspendCoroutine {
        nextStep = SuspendableStep(WaitCondition(cycles), it)
    }

    /**
     * Gets the [ctx] as a [Player]. If [ctx] is not a [Player], a cast exception
     * will be thrown.
     */
    fun player(): Player = ctx as Player
}