package gg.rsmod.game.task

import gg.rsmod.game.model.World
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.service.GameService
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PluginHotswapTask : GameTask {

    private val executor = Executors.newSingleThreadExecutor()

    private var pluginRepository: Future<PluginRepository>? = null

    override fun execute(world: World, service: GameService) {
        if (pluginRepository != null) {
            val newRepository = pluginRepository!!
            if (newRepository.isDone) {
                doSwap(world, newRepository.get())
            }
        } else if (world.hotswapPlugins) {
            world.hotswapPlugins = false
            initiateSwap(world)
        }
    }

    private fun initiateSwap(world: World) {
        pluginRepository = executor.submit(Callable<PluginRepository> {
            val repository = PluginRepository(world)
            repository.initPlugins(jarPluginsDirectory = "./../plugins", analyzeMode = false)
            return@Callable repository
        })
    }

    private fun doSwap(world: World, newRepository: PluginRepository) {
        world.plugins = newRepository
        pluginRepository = null
        println("Swapped plugin repository")
    }
}