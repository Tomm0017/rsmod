package gg.rsmod.plugins.content.areas.alkharid.fishing

import gg.rsmod.plugins.content.skills.fishing.FishingLocation
import gg.rsmod.plugins.content.skills.fishing.FishingSpot
import gg.rsmod.plugins.content.skills.fishing.FishingSpots

val net_spots = FishingLocation()
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3267, 3148, 0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3268,3147,0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3275,3140,0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3276,3140,0)))
net_spots.spots.add(FishingSpot(FishingSpots.NET, Tile(3278,3138,0)))
net_spots.numberOfFishingSpots = 2
net_spots.register()