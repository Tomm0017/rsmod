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
     * The [CoroutineDispatcher] used to schedule suspendable
     */
    private lateinit var dispatcher: CoroutineDispatcher

    private val active = hashSetOf<Plugin>()

    fun init(gameService: GameService) {
        dispatcher = gameService.dispatcher
    }

    fun execute(ctx: Any, logic: Function1<Plugin, Unit>) {
        val plugin = Plugin(ctx, dispatcher)
        active.add(plugin)
        logic.invoke(plugin)
    }

    fun pulse() {
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
}