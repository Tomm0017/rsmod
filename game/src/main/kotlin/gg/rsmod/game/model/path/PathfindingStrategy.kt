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

    fun getPath(origin: Tile, target: Tile, type: EntityType, validSurroundingTiles: Array<Tile>? = null): Queue<Tile> = calculatePath(origin, target, type, validSurroundingTiles)

    protected abstract fun calculatePath(origin: Tile, target: Tile, type: EntityType, validSurroundingTiles: Array<Tile>?): Queue<Tile>
}