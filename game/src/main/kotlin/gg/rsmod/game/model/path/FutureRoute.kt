package gg.rsmod.game.model.path

import com.google.common.util.concurrent.ThreadFactoryBuilder
import gg.rsmod.game.model.MovementQueue
import mu.KLogging
import java.util.concurrent.Executors

/**
 * Represents a [Route] that will be returned some time in the future.
 *
 * @param strategy
 * The [PathFindingStrategy] that will be used to get the path for our route.
 *
 * @param stepType
 * The type of [MovementQueue.StepType] that will be used for the route.
 * Since the route will be handled in the future, there's no guarantee the pawn
 * using it will still have the same state for its [MovementQueue.StepType], which
 * is why we store it on our [FutureRoute].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class FutureRoute private constructor(val strategy: PathFindingStrategy, val stepType: MovementQueue.StepType) {

    /**
     * This flag lets us know if the [FutureRoute.route] has finished.
     */
    @Volatile var completed = false

    lateinit var route: Route

    companion object : KLogging() {

        /**
         * Future routes are handled on a separate thread. This should be defined
         * else-where, but for now we'll keep it here!
         */
        private val executor = Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("pathfinding-thread").setUncaughtExceptionHandler { t, e -> logger.error("Error with thread $t", e) }.build())

        /**
         * Creates a [FutureRoute] with the given parameters and executes it on
         * our [executor] service. When the [Route] is returned, [FutureRoute.completed]
         * is set to true.
         *
         * @param strategy
         * The [PathFindingStrategy] that will be used to get the path for our route.
         *
         * @param request
         * The properties for our path to take.
         *
         * @param stepType
         * The type of [MovementQueue.StepType] that will be used for the route.
         */
        fun of(strategy: PathFindingStrategy, request: PathRequest, stepType: MovementQueue.StepType): FutureRoute {
            val future = FutureRoute(strategy, stepType)
            executor.execute {
                future.route = strategy.calculateRoute(request)
                future.completed = true
            }
            return future
        }
    }
}