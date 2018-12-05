package gg.rsmod.plugins

import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SuspendablePlugin {

    @JvmStatic
    @ScanPlugins
    fun init(r: PluginRepository) {
        r.bindObject(0, 1) {
            GlobalScope.launch(it.dispatcher) {
                it.wait(it.player().world.random(0..4))
            }
        }
    }
}