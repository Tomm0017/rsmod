package gg.rsmod.game.model.path

import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.apache.logging.log4j.LogManager
import java.util.concurrent.Executors

/**
 * @author Tom <rspsmods@gmail.com>
 */
class FutureRoute private constructor() {

    companion object {

        private val logger = LogManager.getLogger(FutureRoute::class.java)
        private val executor = Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("pathfinding-thread").setUncaughtExceptionHandler { t, e -> logger.error("Error with thread $t", e) }.build())

        fun of(strategy: PathFindingStrategy, request: PathRequest): FutureRoute {
            val future = FutureRoute()
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