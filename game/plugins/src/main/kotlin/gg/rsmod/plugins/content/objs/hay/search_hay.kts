package gg.rsmod.plugins.content.objs.hay

import com.google.common.collect.ImmutableSet
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.queue.QueueTask

val HAY_OBJECTS = ImmutableSet.of(Objs.HAYSTACK, Objs.HAY_BALES, Objs.HAY_BALES_299)

HAY_OBJECTS.forEach { hay ->
    on_obj_option(obj = hay, option = "search") {
        val obj = player.getInteractingGameObj()
        val name = obj.getDef(player.world.definitions).name
        player.queue { search(this, player, name.toLowerCase()) }
    }
}

suspend fun search(it: QueueTask, p: Player, obj: String) {
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