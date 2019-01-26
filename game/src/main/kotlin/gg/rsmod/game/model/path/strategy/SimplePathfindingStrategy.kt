package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.path.PathfindingStrategy
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SimplePathfindingStrategy(override val world: World) : PathfindingStrategy(world) {

    override fun calculateRoute(start: Tile, end: Tile, type: EntityType, sourceWidth: Int, sourceLength: Int,
                                targetWidth: Int, targetLength: Int, invalidBorderTile: (Tile) -> (Boolean)): Route {
        val path = ArrayDeque<Tile>()
        return Route(path, true)
    }
}