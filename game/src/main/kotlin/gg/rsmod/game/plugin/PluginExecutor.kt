package gg.rsmod.game.plugin

import gg.rsmod.game.service.GameService
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Handles the boiler-plate code needed to execute a plugin.
 *
 * @author Tom
 */
class PluginExecutor {

    /**
     * The [CoroutineDispatcher] used to schedule suspendable plugins.
     */
    private lateinit var dispatcher: CoroutineDispatcher

    /**
     * A collection of all 'active' plugins that are being executed.
     */
    private val active = hashSetOf<Plugin>()

    fun init(gameService: GameService) {
        dispatcher = gameService.dispatcher
    }

    /**
     * Get the amount of active plugins in the executor.
     */
    fun getActiveCount(): Int = active.size

    internal fun <T> execute(ctx: Any, logic: (Plugin) -> T): T? {
        val plugin = Plugin(ctx, dispatcher)
        val invoke = logic(plugin)

        if (plugin.suspended()) {
            /**
             * NOTE:
             *
             * We may need to redo a core part of the [PluginExecutor] in the future.
             * This is due to the limitations with the current one:
             *
             * Problem:
             *
             * The [logic] must be invoked in order to know if the plugin is
             * suspended. Any previous suspended plugin should be interrupted
             * before [logic] is invoked. This cannot be achieved with the current
             * system.
             *
             * In-depth problem:
             *
             * In an ideal system, we would be able to know if the plugin will be
             * suspended ahead-of-time, so that we can interrupt any previous
             * plugin in our [active] list first, and then execute the [logic]
             * afterwards. Currently, the logic must be executed first before the
             * system knows if the plugin is suspended or not; because of this
             * we cannot interrupt the previous 'active' plugin, which in some
             * cases we may want.
             *
             * Example of problem:
             *
             * If there's an important plugin that <strong>must</strong> invoke
             * its [Plugin.interruptAction], no matter how it's terminated - with
             * the current system we can't deliver that promise, as executing
             * another suspendable plugin will simply remove it from the [active],
             * but not interrupt it.
             *
             * - "Why not just interrupt the previous plugin instead of removing?"
             *
             * It is because certain plugins like dialog plugins will remove the
             * chatbox interface upon interruption. So if we have the initial plugin
             * show the chatbox interface and then execute another dialog plugin
             * from somewhere else, the second plugin's logic will be invoked first,
             * and then the first plugin will be interrupted - this leads to the
             * chatbox interface not showing up at all for the second plugin.
             */
            active.removeIf { it.ctx == ctx }
            active.add(plugin)
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
    internal fun submitReturnType(ctx: Any, value: Any) {
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
    internal fun interruptPluginsWithContext(ctx: Any) {
        val iterator = active.iterator()
        while (iterator.hasNext()) {
            val plugin = iterator.next()
            if (plugin.ctx == ctx) {
                plugin.terminate()
                iterator.remove()
            }
        }
    }

    internal fun pulse() {
        /**
         * Copy the active list to avoid concurrent modifications if one plugin
         * executes another.
         */
        val plugins = active.toList()

        plugins.forEach { plugin ->
            plugin.cycle()
            if (!plugin.suspended()) {
                active.remove(plugin)
            }
        }
    }

    /**
     * Removes all active and queued plugins.
     */
    internal fun killAll() {
        active.clear()
    }
}