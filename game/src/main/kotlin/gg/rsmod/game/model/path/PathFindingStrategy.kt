package gg.rsmod.game.model.path

import gg.rsmod.game.model.collision.CollisionManager

/**
 * Represents an strategy that can be used to find a valid path to a target.
 *
 * @param collision
 * The [CollisionManager] that will be used for any and all clipping flag states
 * in our path finder.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PathFindingStrategy(val collision: CollisionManager) {

    /**
     * If the path finder should cancel its search.
     *
     * Used to cancel [FutureRoute]s if its no longer required.
     */
    @Volatile var cancel = false

    /**
     * Calculate the most appropriate route given a [PathRequest].
     */
    abstract fun calculateRoute(request: PathRequest): Route

    companion object {
        /**
         * The maximum distance, in tiles, between the origin and target tiles.
         */
        const val MAX_DISTANCE = 20
    }
}