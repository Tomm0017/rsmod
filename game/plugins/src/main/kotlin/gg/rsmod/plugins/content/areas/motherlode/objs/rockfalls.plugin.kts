package gg.rsmod.plugins.content.areas.motherlode.objs

arrayOf(Objs.ROCKFALL, Objs.ROCKFALL_26680, Objs.ROCKFALL_28786, Objs.ROCKFALL_32503).forEach { rockfall ->
    on_obj_option(rockfall, 1) {
        mineRockFall(player)
    }
}

fun mineRockFall(player: Player) {
    player.queue {
        player.animate(id = 6746, delay = 15)
        val rock = player.getInteractingGameObj()
        world.queue {
            world.remove(rock)
            wait(20)
            world.spawn(DynamicObject(rock, rock.id))
        }
        wait(1)
        player.animate(id = -1, delay = 0)
    }
}