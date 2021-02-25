package gg.rsmod.plugins.content.areas.motherlode.objs

import gg.rsmod.game.model.region.Region

on_world_init {
    val motherRegion = Region(14936)

    motherRegion.tiles.forEach { tile ->
        val wall = (world.getObject(tile, type = 0))
        if(wall != null && wall.id in Objs.DEPLETED_VEIN_26665..Objs.DEPLETED_VEIN_26668){
            val vein = wall.id - 4
            world.remove(wall)
            world.spawn(DynamicObject(id = vein, type = 0, rot = wall.rot, tile = tile))
        }
    }
}