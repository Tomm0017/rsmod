package gg.rsmod.plugins.content.areas.draynor.fishing

import gg.rsmod.plugins.content.skills.fishing.FishingLocation
import gg.rsmod.plugins.content.skills.fishing.FishingSpot
import gg.rsmod.plugins.content.skills.fishing.FishingSpots

val net_spots = FishingLocation()
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3086, 3227, 0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3085,3230,0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3086,3228,0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3085,3231,0)))
net_spots.numberOfFishingSpots = 2
net_spots.register()