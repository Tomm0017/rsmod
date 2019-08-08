package gg.rsmod.plugins.content.skills.fishing

import gg.rsmod.game.model.Tile

class FishingLocation {
    var spots: MutableList<FishingSpot> = mutableListOf()
    var numberOfFishingSpots = 1

    fun register() {
        FishingManager.fishing_locations.add(this)
    }
}