package gg.rsmod.plugins.content.areas.barbvillage.fishing

import gg.rsmod.plugins.content.skills.fishing.FishingLocation
import gg.rsmod.plugins.content.skills.fishing.FishingSpot
import gg.rsmod.plugins.content.skills.fishing.FishingSpots

val lure_spots = FishingLocation()
lure_spots.spots.add(FishingSpot(FishingSpots.LURE2, Tile(3110, 3432, 0)))
lure_spots.spots.add(FishingSpot(FishingSpots.LURE2, Tile(3104,3424,0)))
lure_spots.spots.add(FishingSpot(FishingSpots.LURE2, Tile(3110,3433,0)))
lure_spots.spots.add(FishingSpot(FishingSpots.LURE2, Tile(3110,3434,0)))
lure_spots.spots.add(FishingSpot(FishingSpots.LURE2, Tile(3104,3425,0)))
lure_spots.numberOfFishingSpots = 2
lure_spots.register()