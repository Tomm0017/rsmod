package gg.rsmod.plugins.content.objs.cabbage

val RESPAWN_DELAY = 75

on_obj_option(obj = Objs.CABBAGE_1161, option = "pick", lineOfSightDistance = 0) {
    val obj = player.getInteractingGameObj()

    player.queue {
        val route = player.walkTo(this, obj.tile)
        if (route.success) {
            if (player.inventory.isFull) {
                player.message("You don't have room for this cabbage.")
                return@queue
            }
            if (obj.isSpawned(world)) {
                val item = if (world.percentChance(5.0)) Items.CABBAGE_SEED else Items.CABBAGE
                player.animate(827)
                player.inventory.add(item = item)
                world.remove(obj)
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