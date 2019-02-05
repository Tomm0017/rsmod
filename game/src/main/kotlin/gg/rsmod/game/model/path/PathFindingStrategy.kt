package gg.rsmod.game.model.path

import gg.rsmod.game.model.World

/**
 * Represents an strategy that can be used to find a valid path to a target.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PathFindingStrategy(val world: World) {

    companion object {
        /**
         * The maximum distance between the origin and target tiles.
         */
        const val MAX_DISTANCE = 20
    }

    abstract fun calculateRoute(request: PathRequest): Route
}