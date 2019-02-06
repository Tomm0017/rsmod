package gg.rsmod.game.model.path

import com.google.common.util.concurrent.ThreadFactoryBuilder
import gg.rsmod.game.model.MovementQueue
import org.apache.logging.log4j.LogManager
import java.util.concurrent.Executors

/**
 * @author Tom <rspsmods@gmail.com>
 */
class FutureRoute private constructor(val strategy: PathFindingStrategy, val stepType: MovementQueue.StepType) {

    companion object {

        private val logger = LogManager.getLogger(FutureRoute::class.java)

        private val executor = Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("pathfinding-thread").setUncaughtExceptionHandler { t, e -> logger.error("Error with thread $t", e) }.build())

        fun of(strategy: PathFindingStrategy, request: PathRequest, stepType: MovementQueue.StepType): FutureRoute {
            val future = FutureRoute(strategy, stepType)
            executor.execute {
                future.route = strategy.calculateRoute(request)
                future.completed = true
            }
            return future
        }
    }

    @Volatile var completed = false

    lateinit var route: Route
}