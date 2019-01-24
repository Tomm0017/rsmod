package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.path.PathfindingStrategy
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SimplePathfindingStrategy(override val world: World) : PathfindingStrategy(world) {

    override fun calculatePath(start: Tile, target: Tile, type: EntityType): Queue<Tile> {
        val path = ArrayDeque<Tile>()
        path.add(start)

        val dx = start.x - target.x
        val dz = start.z - target.z

        var dir = if (dx > 0) Direction.WEST else if (dx < 0) Direction.EAST else null
        if (dir != null) {
            var tile = Tile(start)

            while (world.collision.canTraverse(tile, dir, type) && tile.x != target.x) {
                tile = tile.transform(dir.getDeltaX(), 0)
                path.addLast(tile)
            }
        }

        dir = if (dz > 0) Direction.SOUTH else if (dz < 0) Direction.NORTH else null
        if (dir != null) {
            var tile = Tile(start)

            while (world.collision.canTraverse(tile, dir, type) && tile.z != target.z) {
                tile = tile.transform(0, dir.getDeltaZ())
                path.addLast(tile)
            }
        }

        return path
    }
}