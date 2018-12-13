package gg.rsmod.game.model.path

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.GameObject
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PathfindingStrategy(open val world: World) {

    companion object {
        /**
         * The maximum distance between the origin and target tiles.
         */
        const val MAX_DISTANCE = 24
    }

    fun getPath(origin: Tile, target: Tile, type: EntityType): Queue<Tile> {
        return calculatePath(origin, target, type)
    }

    fun getPath(origin: Tile, obj: GameObject, type: EntityType): Queue<Tile> {
        return calculatePath(origin, obj, type)
    }

    protected abstract fun calculatePath(origin: Tile, target: Tile, type: EntityType): Queue<Tile>

    protected abstract fun calculatePath(origin: Tile, obj: GameObject, type: EntityType): Queue<Tile>
}