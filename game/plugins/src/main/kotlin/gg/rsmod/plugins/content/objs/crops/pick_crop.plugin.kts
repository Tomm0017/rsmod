package gg.rsmod.plugins.content.objs.crops

val RESPAWN_DELAY = 75

data class Crop(val name: String, val objs: Set<Int>, val item: (Player) -> Int)

val crops = setOf(
        Crop(
                "cabbage",
                setOf(Objs.CABBAGE_1161)
        ) { player -> if (player.world.percentChance(5.0)) Items.CABBAGE_SEED else Items.CABBAGE },
        Crop(
                "wheat",
                setOf(Objs.WHEAT_15506, Objs.WHEAT_15507, Objs.WHEAT_15508)
        ) { Items.GRAIN },
        Crop(
                "potato",
                setOf(Objs.POTATO_312)
        ) { Items.POTATO }

)

for (crop in crops) {
    for (objId in crop.objs) {
        on_obj_option(objId, option = "pick", lineOfSightDistance = 0) {
            val obj = player.getInteractingGameObj()

            player.queue {
                val route = player.walkTo(this, obj.tile)
                if (route.success) {
                    if (player.inventory.isFull) {
                        player.message("You don't have room for this ${crop.name}.")
                        return@queue
                    }

                    if (obj.isSpawned(world)) {
                        val item = crop.item(player)
                        player.animate(827)
                        player.inventory.add(item = item)
                        world.remove(obj)
                        world.queue {
                            spawn_obj(45774, 1441, 144)
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
}
