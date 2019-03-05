package gg.rsmod.plugins.content.areas.edgeville.objs

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.getInteractingGameObj
import gg.rsmod.plugins.api.ext.player

on_obj_option(Objs.LEVER_26761, "pull") {
    val obj = getInteractingGameObj()

    player.queue {
        player.lock()
        wait(1)
        player.animate(2140)
        player.message("You pull the lever...")
        wait(1)

        player.world.queue {
            val world = ctx as World
            val pulled = DynamicObject(obj, 88)
            world.remove(obj)
            world.spawn(pulled)
            wait(10)
            world.remove(pulled)
            world.spawn(DynamicObject(obj))
        }

        wait(2)

        player.animate(714)
        player.graphic(111, 110)
        wait(4)

        player.animate(-1)
        player.teleport(3154, 3924)
        player.message("... and teleport into the Wilderness.")
        player.unlock()
    }
}