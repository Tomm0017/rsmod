package gg.rsmod.game.model.path

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.collision.CollisionManager
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PathfindingStrategy(open val collision: CollisionManager) {

    companion object {
        /**
         * The maximum distance between the origin and target tiles.
         */
        const val MAX_DISTANCE = 24
    }

    fun getPath(origin: Tile, target: Tile, type: EntityType): PathRequest {
        val result = PathRequest()
        result.path = calculatePath(origin, target, type, result)
        return result
    }

    protected abstract fun calculatePath(origin: Tile, target: Tile, type: EntityType, request: PathRequest): Queue<Tile>
}