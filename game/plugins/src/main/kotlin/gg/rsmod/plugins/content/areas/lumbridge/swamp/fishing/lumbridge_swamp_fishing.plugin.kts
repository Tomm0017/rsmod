package gg.rsmod.plugins.content.areas.lumbridge.swamp.fishing

import gg.rsmod.plugins.content.skills.fishing.FishingLocation
import gg.rsmod.plugins.content.skills.fishing.FishingSpot
import gg.rsmod.plugins.content.skills.fishing.FishingSpots

val net_spots = FishingLocation()
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3246, 3155, 0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3245,3152,0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3244,3150,0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3242,3148,0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3242,3143,0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3248,3161,0)))
net_spots.numberOfFishingSpots = 3
net_spots.register()