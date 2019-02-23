package gg.rsmod.game.task

import com.google.common.util.concurrent.ThreadFactoryBuilder
import gg.rsmod.game.model.World
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.service.GameService
import mu.KotlinLogging
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PluginHotswapTask : GameTask {

    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    private val executor = Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("plugin-hotswap-thread").setUncaughtExceptionHandler { t, e -> logger.error("Error with thread $t", e)  }.build())

    private var pluginRepository: Future<PluginRepository>? = null

    override fun execute(world: World, service: GameService) {
        if (pluginRepository != null) {
            val newRepository = pluginRepository!!
            if (newRepository.isDone) {
                doSwap(world, newRepository.get())
                logger.info { "Plugins have been hot-swapped." }
            }
        } else if (world.hotswapPlugins) {
            world.hotswapPlugins = false
            initiateSwap(world)
            logger.info { "Plugin hot-swap has initiated." }
        }
    }

    private fun initiateSwap(world: World) {
        pluginRepository = executor.submit(Callable<PluginRepository> {
            val repository = PluginRepository(world)
            repository.loadPlugins(jarPluginsDirectory = "./../plugins", analyzeMode = false)
            return@Callable repository
        })
    }

    private fun doSwap(world: World, newRepository: PluginRepository) {
        world.plugins = newRepository
        pluginRepository = null
    }
}