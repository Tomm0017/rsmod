package gg.rsmod.plugins.content.areas.shared.objs

on_obj_option(Objs.LEVER_26761, "pull") {
    handleLeverInteraction {
        moveTo(3154, 3924)
        message("... and teleport into the Wilderness.")
    }
}
on_obj_option(Objs.LEVER_1814, "pull") {
    handleLeverInteraction {
        moveTo(3154, 3924)
        message("... and teleport into the Wilderness.")
    }
}
on_obj_option(Objs.LEVER_9472, "pull") {
    handleLeverInteraction {
        moveTo(2561, 3311)
        message("... and teleport to Ardougne.")
    }
}

fun Plugin.handleLeverInteraction(onAnimationComplete: Player.() -> Unit) {
    val obj = player.getInteractingGameObj()

    player.queue {
        player.lock()
        wait(1)
        player.animate(2140)
        player.message("You pull the lever...")
        wait(1)

        world.queue {
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
        onAnimationComplete(player)
        player.unlock()
    }
}
