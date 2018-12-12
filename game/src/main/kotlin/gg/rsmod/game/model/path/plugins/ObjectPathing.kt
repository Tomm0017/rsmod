package gg.rsmod.game.model.path.plugins

import gg.rsmod.game.model.INTERACTING_OBJ_ATTR
import gg.rsmod.game.model.INTERACTING_OPT_ATTR
import gg.rsmod.game.plugin.Plugin

/**
 * @author Tom <rspsmods@gmail.com>
 */
object ObjectPathing {

    val walkPlugin: (Plugin) -> Unit = {
        val p = it.player()

        val obj = p.attr[INTERACTING_OBJ_ATTR]
        val opt = p.attr[INTERACTING_OPT_ATTR]

        if (!p.world.plugins.executeObject(p, obj.id, opt)) {
            p.message("Nothing interesting happens.")
            if (p.world.gameContext.devMode) {
                p.message("Unhandled object action: [opt=$opt, id=${obj.id}, x=${obj.tile.x}, z=${obj.tile.z}]")
            }
        }
    }
}