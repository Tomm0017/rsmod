package gg.rsmod.game.plugin

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.coroutine.*
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

    /**
     * A value that can be requested by a plugin, such as an input for dialogs.
     */
    var requestReturnValue: Any? = null

    /**
     * This flag indicates whether or not [PluginExecutor.pulse] has iterated
     * through this plugin at least once, as the [pulse] must be handled one
     * tick after the plugin logic has executed.
     */
    var started = false

    /**
     * Can represent an action that should be executed if, and only if, this plugin
     * was interrupted by another action such as walking or a new script being
     * executed by the same [ctx].
     */
    var interruptAction: Function1<Plugin, Unit>? = null

    /**
     * The next [SuspendableStep], if any, that must be handled once a [SuspendableCondition]
     * returns [SuspendableCondition.resume] as [true].
     */
    private var nextStep: SuspendableStep? = null

    /**
     * The [CoroutineContext] implementation for our [Plugin].
     */
    override val context: CoroutineContext = EmptyCoroutineContext

    /**
     * When the [nextStep] [SuspendableCondition.resume] returns true, this
     * method is called.
     */
    override fun resumeWith(result: Result<Unit>) {
        nextStep = null
    }

    /**
     * Boilerplate to signal the use of suspendable logic.
     */
    fun suspendable(block: suspend CoroutineScope.() -> Unit) {
        val coroutine = block.createCoroutine(receiver = CoroutineScope(dispatcher), completion = this)
        coroutine.resume(Unit)
    }

    /**
     * The logic in each [SuspendableStep] must be game-thread-safe, so we use pulse
     * method to keep them in-sync.
     */
    fun pulse() {
        val next = nextStep ?: return

        if (next.condition.resume()) {
            next.continuation.resume(Unit)
            requestReturnValue = null
        }
    }

    /**
     * Terminate any further execution of this plugin, at any state.
     */
    fun terminate() {
        nextStep = null
        requestReturnValue = null
        interruptAction?.invoke(this)
    }

    /**
     * If the [PluginExecutor] is allowed to remove the plugin from its active
     * plugin collection.
     */
    fun canKill(): Boolean = nextStep == null

    /**
     * Wait for the specified amount of game cycles [cycles] before
     * continuing the logic associated with this plugin.
     */
    suspend fun wait(cycles: Int): Unit = suspendCoroutine {
        check(cycles > 0) { "Wait cycles must be greater than 0." }
        nextStep = SuspendableStep(WaitCondition(cycles), it)
    }

    /**
     * Wait for our [ctx] to reach [tile]. Note that [ctx] MUST be an instance
     * of [Pawn] and that the height of the [tile] and [Pawn.tile] must be equal,
     * as well as the x and z coordinates.
     */
    suspend fun waitTile(tile: Tile): Unit = suspendCoroutine {
        nextStep = SuspendableStep(TileCondition(pawn().tile, tile), it)
    }

    /**
     * Wait for a return value to be available before continuing.
     */
    suspend fun waitReturnValue(): Unit = suspendCoroutine {
        nextStep = SuspendableStep(PredicateCondition { requestReturnValue != null }, it)
    }

    /**
     * Gets the [ctx] as a [Player]. If [ctx] is not a [Player], a cast exception
     * will be thrown.
     */
    fun player(): Player = ctx as Player

    /**
     * Gets the [ctx] as a [Pawn]. If [ctx] is not a [Pawn], a cast exception
     * will be thrown.
     */
    fun pawn(): Pawn = ctx as Pawn
}