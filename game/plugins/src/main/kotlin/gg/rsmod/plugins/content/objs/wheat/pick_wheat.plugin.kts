package gg.rsmod.plugins.content.objs.wheat

val RESPAWN_DELAY = 30//42

private val WHEATS = setOf(Objs.WHEAT, Objs.WHEAT_5583, Objs.WHEAT_5584, Objs.WHEAT_5585, Objs.WHEAT_15506,
        Objs.WHEAT_15507, Objs.WHEAT_15508)

WHEATS.forEach { wheat ->
    on_obj_option(obj = wheat, option = "pick", lineOfSightDistance = 0) {
        val obj = player.getInteractingGameObj()

        player.queue {
            val route = player.walkTo(this, obj.tile)
            if (route.success) {
                if (player.inventory.isFull) {
                    player.message("You can't carry any more grain.")
                    return@queue
                }
                if (obj.isSpawned(world)) {
                    player.animate(id = 827)
                    player.inventory.add(item = Items.GRAIN)
                    world.remove(obj)
                    player.message("You pick some grain.")
                    world.queue {
                        wait(RESPAWN_DELAY)
                        world.spawn(DynamicObject(obj))
                    }
                }
            } else {
                player.message(Entity.YOU_CANT_REACH_THAT)
            }
        }
    }
}