package gg.rsmod.game.model.path

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.entity.EntityType
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PathfindingStrategy(open val collision: CollisionManager) {

    abstract fun getPath(origin: Tile, target: Tile, type: EntityType): Deque<Tile>
}