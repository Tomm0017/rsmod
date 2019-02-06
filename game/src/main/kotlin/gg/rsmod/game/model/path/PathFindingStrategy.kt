package gg.rsmod.game.model.path

import gg.rsmod.game.model.collision.CollisionManager

/**
 * Represents an strategy that can be used to find a valid path to a target.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PathFindingStrategy(val collision: CollisionManager) {

    companion object {
        /**
         * The maximum distance between the origin and target tiles.
         */
        const val MAX_DISTANCE = 20
    }

    @Volatile var cancel = false

    abstract fun calculateRoute(request: PathRequest): Route
}