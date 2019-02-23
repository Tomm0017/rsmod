package gg.rsmod.plugins.content.objs.hay

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.chatPlayer
import gg.rsmod.plugins.api.ext.getInteractingGameObj
import gg.rsmod.plugins.api.ext.hit
import gg.rsmod.plugins.api.ext.player

val HAY_OBJECTS = arrayOf(Objs.HAYSTACK, Objs.HAY_BALES, Objs.HAY_BALES_299)

HAY_OBJECTS.forEach { hay ->
    on_obj_option(obj = hay, option = "search") {
        val player = it.player()
        val obj = it.getInteractingGameObj()
        val name = obj.getDef(player.world.definitions).name
        it.suspendable { search(it, player, name.toLowerCase()) }
    }
}

suspend fun search(it: Plugin, p: Player, obj: String) {
    p.lock()
    p.message("You search the $obj...")
    p.animate(827)
    it.wait(3)
    p.unlock()
    when (p.world.random(100)) {
        0 -> {
            val add = p.inventory.add(item = Items.NEEDLE)
            if (add.hasFailed()) {
                p.world.spawn(GroundItem(item = Items.NEEDLE, amount = 1, tile = Tile(p.tile), owner = p))
            }
            it.chatPlayer("Wow! A needle!<br>Now what are the chances of finding that?")
        }
        1 -> {
            p.hit(damage = 1)
            p.forceChat("Ouch!")
        }
        else -> p.message("You find nothing of interest.")
    }
}