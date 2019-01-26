package gg.rsmod.game.model.path

import gg.rsmod.game.model.Tile
import java.util.*

data class Route(val path: ArrayDeque<Tile>, val success: Boolean, val tail: Tile)