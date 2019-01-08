package gg.rsmod.game.plugin

import gg.rsmod.game.service.GameService
import kotlinx.coroutines.CoroutineDispatcher
import org.apache.logging.log4j.LogManager

/**
 * Handles the boiler-plate code needed to execute a plugin.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PluginExecutor {

    companion object {
        private val logger = LogManager.getLogger(PluginExecutor::class.java)
    }

    /**
     * The [CoroutineDispatcher] used to schedule suspendable plugins.
     */
    private lateinit var dispatcher: CoroutineDispatcher

    /**
     * A collection of all 'active' plugins that are being executed.
     */
    private val active = hashSetOf<Plugin>()

    /**
     * A collection of plugins that will be executed on the next possible
     * cycle.
     */
    private val activeQueue = hashSetOf<Plugin>()

    fun init(gameService: GameService) {
        dispatcher = gameService.dispatcher
    }

    /**
     * Get the amount of active plugins in the executor.
     */
    fun getActiveCount(): Int = active.size

    fun <T> execute(ctx: Any, logic: Function1<Plugin, T>): T {
        val plugin = Plugin(ctx, dispatcher)
        val invoke = logic.invoke(plugin)

        /**
         * We only categorize the plugin as 'active' if the plugin has been
         * suspended. This is to avoid non-suspendable plugins from removing
         * suspendable plugins.
         *
         * For example, if we didn't do this, a simple timer plugin, such as a
         * prayer drain timer, which isn't suspendable but executed every cycle,
         * would cancel suspendable plugins such as dialogs. (so you wouldn't be
         * able to continue the dialog as it has been removed from 'active'
         * plugins and would no longer pulse).
         */
        if (!plugin.canKill()) {
            activeQueue.add(plugin)
        }

        return invoke
    }

    /**
     * In-game events sometimes must return a value to a plugin. An example are
     * dialogs which must return values such as input, button click, etc.
     *
     * @param ctx
     * The context that submitted the initial plugin.
     *
     * @param value
     * The return value that the plugin has asked for.
     */
    fun submitReturnType(ctx: Any, value: Any) {
        val iterator = active.iterator()
        while (iterator.hasNext()) {
            val plugin = iterator.next()
            if (plugin.ctx == ctx) {
                plugin.requestReturnValue = value
            }
        }
    }

    /**
     * Terminates any plugins that have [ctx] as their context.
     */
    fun interruptPluginsWithContext(ctx: Any) {
        val iterator = active.iterator()
        while (iterator.hasNext()) {
            val plugin = iterator.next()
            if (plugin.ctx == ctx) {
                plugin.terminate()
                iterator.remove()
            }
        }
    }

    fun pulse() {
        active.addAll(activeQueue)
        activeQueue.clear()

        val iterator = active.iterator()
        while (iterator.hasNext()) {
            val plugin = iterator.next()
            /**
             * The first pulse must be completely skipped, otherwise the initial
             * logic executes 1-tick too soon.
             */
            if (plugin.started) {
                plugin.pulse()
                if (plugin.canKill()) {
                    iterator.remove()
                }
            } else {
                plugin.started = true
            }
        }
    }

    /**
     * Removes all active and queued plugins.
     *
     * This method is reserved for internal use. Avoid using.
     */
    fun internalKillAll() {
        active.clear()
        activeQueue.clear()
    }
}