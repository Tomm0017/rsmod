package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.PathfindingStrategy
import gg.rsmod.game.model.path.Route
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SimplePathfindingStrategy(override val world: World) : PathfindingStrategy(world) {

    override fun calculateRoute(request: PathRequest): Route {
        val path = ArrayDeque<Tile>()
        return Route(path, false, request.start)
    }
}