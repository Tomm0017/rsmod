package gg.rsmod.plugins.content.skills.fishing

// Spawn all of the fishing npcs from manager
on_world_init {

    FishingManager.fishing_locations.forEach { location ->
        var count = 1
        while(count <= location.numberOfFishingSpots) {
            world.spawn(Npc(location.spots.get(count-1).spot.spotEntityId, location.spots.get(count-1).tile, world))
            count++
        }
    }


    // Register listener events on fishing spots
    FishingSpots.values().forEach { spot ->
        on_npc_option(spot.spotEntityId, spot.option) {
            val fishing = Fishing(player, spot)
            player.queue { fishing.startFishing(this) }
        }
    }
}