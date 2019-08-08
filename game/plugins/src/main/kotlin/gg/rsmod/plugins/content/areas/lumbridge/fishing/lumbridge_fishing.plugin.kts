package gg.rsmod.plugins.content.areas.lumbridge.fishing

import gg.rsmod.plugins.content.skills.fishing.FishingLocation
import gg.rsmod.plugins.content.skills.fishing.FishingSpot
import gg.rsmod.plugins.content.skills.fishing.FishingSpots

val lure_spots = FishingLocation()
lure_spots.spots.add(FishingSpot(FishingSpots.LURE, Tile(3238, 3254, 0)))
lure_spots.spots.add(FishingSpot(FishingSpots.LURE, Tile(3239,3243,0)))
lure_spots.spots.add(FishingSpot(FishingSpots.LURE, Tile(3239,3244,0)))
lure_spots.spots.add(FishingSpot(FishingSpots.LURE, Tile(3239,3241,0)))
lure_spots.spots.add(FishingSpot(FishingSpots.LURE, Tile(3238,3251,0)))
lure_spots.spots.add(FishingSpot(FishingSpots.LURE, Tile(3239,3242,0)))
lure_spots.spots.add(FishingSpot(FishingSpots.LURE, Tile(3238,3255,0)))
lure_spots.numberOfFishingSpots = 2
lure_spots.register()