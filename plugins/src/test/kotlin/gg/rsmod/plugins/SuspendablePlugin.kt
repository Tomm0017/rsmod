package gg.rsmod.plugins

import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SuspendablePlugin {

    @JvmStatic
    @ScanPlugins
    fun init(r: PluginRepository) {
        r.bindObject(0, 1) {

        }
    }
}