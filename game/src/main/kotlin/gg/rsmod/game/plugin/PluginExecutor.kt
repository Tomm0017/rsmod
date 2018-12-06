package gg.rsmod.game.plugin

import gg.rsmod.game.service.GameService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
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
        logic.invoke(plugin)
        active.add(plugin)
    }

    fun pulse() = runBlocking {
        val iterator = active.iterator()
        while (iterator.hasNext()) {
            val plugin = iterator.next()
            println("plugin state: ${plugin.state}")
            if (plugin.state == PluginState.NORMAL) {
                iterator.remove()
            } else if (plugin.state == PluginState.TIME_WAIT) {
                if (plugin.waitCycles > 0) {
                    --plugin.waitCycles
                }
                if (plugin.currentJob!!.isCompleted) {
                    plugin.state = PluginState.NORMAL
                } else if (plugin.currentJob!!.isCancelled) {
                    plugin.state = PluginState.INTERRUPTED
                }
            }
        }
    }
}