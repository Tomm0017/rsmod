package gg.rsmod.game.plugin

import gg.rsmod.game.model.entity.Player
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay

/**
 * Represents a plugin that can be executed at any time by a context.
 *
 * @param ctx Can be anything from [Player] to [gg.rsmod.game.model.entity.Pawn].
 * @param cycleTime The time in between each full game cycle, in milliseconds.
 * @param dispatcher The coroutine dispatcher.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Plugin(val ctx: Any?, val cycleTime: Int, val dispatcher: CoroutineDispatcher) {

    /**
     * The current state of the plugin that allows us to keep track of suspended
     * or interrupted plugins.
     */
    var state = PluginState.NORMAL

    /**
     * Can represent an action that should be executed if, and only if, this plugin
     * was interrupted by another action such as walking or a new script being
     * executed by the same [ctx].
     */
    var interruptAction: Function0<Unit>? = null

    /**
     * Wait for the specified amount of game cycles [cycles] before
     * continuing the logic associated with this plugin.
     */
    suspend fun wait(cycles: Int) {
        state = PluginState.TIME_WAIT

        var left = cycles
        while (left-- > 0) {
            delay(cycleTime)
        }
    }

    /**
     * Gets the [ctx] as a [Player]. If [ctx] is not a [Player], a cast exception
     * will be thrown.
     */
    fun player(): Player = ctx as Player
}