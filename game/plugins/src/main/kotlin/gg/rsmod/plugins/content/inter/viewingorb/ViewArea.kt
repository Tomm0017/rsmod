package gg.rsmod.plugins.content.inter.viewingorb

import gg.rsmod.game.model.Tile
import gg.rsmod.plugins.content.skills.cooking.data.CookingFood

enum class ViewArea(val gameArea : String, val center : Tile, val northWest : Tile, val northEast : Tile, val southEast : Tile, val southWest : Tile) {
    FOREST(
            gameArea = "forest",
            center = Tile(123,123),
            northWest = Tile(123,123),
            northEast = Tile(123,123) ,
            southEast= Tile(123,123),
            southWest = Tile(123,123)),
    SNOW(
            gameArea = "snow",
            center = Tile(123,123),
            northWest = Tile(123,123),
            northEast = Tile(123,123),
            southEast= Tile(123,123),
            southWest = Tile(123,123)),
    DESERT(
            gameArea = "desert",
            center = Tile(123,123),
            northWest = Tile(123,123),
            northEast = Tile(123,123),
            southEast= Tile(123,123),
            southWest = Tile(123,123))

}