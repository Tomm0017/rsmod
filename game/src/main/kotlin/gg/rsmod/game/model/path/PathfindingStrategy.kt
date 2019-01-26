package gg.rsmod.game.model.path

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import java.util.*

/**
 * Represents an strategy that can be used to find a valid path to a target.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PathfindingStrategy(open val world: World) {

    companion object {
        /**
         * The maximum distance between the origin and target tiles.
         */
        const val MAX_DISTANCE = 20
    }

    abstract fun calculateRoute(start: Tile, end: Tile, type: EntityType, sourceWidth: Int, sourceLength: Int,
                                targetWidth: Int, targetLength: Int, invalidBorderTile: (Tile) -> (Boolean)): Route

    data class Route(val path: ArrayDeque<Tile>, val success: Boolean)
}