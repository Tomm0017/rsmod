package gg.rsmod.plugins.content.objs.bookcase

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.player

val BOOKCASES = arrayOf(Objs.BOOKCASE_380, Objs.BOOKCASE_381)

BOOKCASES.forEach { case ->
    on_obj_option(obj = case, option = "search") {
        it.suspendable { search(it, it.player()) }
    }
}

suspend fun search(it: Plugin, p: Player) {
    p.lock()
    p.message("You search the books...")
    it.wait(3)
    p.unlock()
    p.message("You don't find anything that you'd ever want to read.")
}