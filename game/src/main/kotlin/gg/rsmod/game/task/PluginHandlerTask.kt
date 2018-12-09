package gg.rsmod.game.task

import gg.rsmod.game.model.World
import gg.rsmod.game.plugin.PluginExecutor
import gg.rsmod.game.service.GameService

/**
 * A [GameTask] responsible for executing the [PluginExecutor] pulse.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PluginHandlerTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.pluginExecutor.pulse()
    }
}