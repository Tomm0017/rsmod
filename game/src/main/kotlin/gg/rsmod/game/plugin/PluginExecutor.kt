package gg.rsmod.game.plugin

import gg.rsmod.game.service.GameService
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Handles the boiler-plate code needed to execute a plugin.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PluginExecutor {

    /**
     * The [CoroutineDispatcher] used to schedule suspendable
     */
    lateinit var dispatcher: CoroutineDispatcher

    /**
     * The amount of time, in milliseconds, in between a full game cycle.
     */
    var cycleTime = 0

    fun init(gameService: GameService) {
        dispatcher = gameService.dispatcher
        cycleTime = gameService.world.gameContext.cycleTime
    }

    fun execute(ctx: Any, logic: Function1<Plugin, Unit>) {
        val plugin = Plugin(ctx, cycleTime, dispatcher)
        logic.invoke(plugin)
    }
}