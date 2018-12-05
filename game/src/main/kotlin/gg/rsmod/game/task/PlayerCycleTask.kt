package gg.rsmod.game.task

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService

/**
 * A [GameTask] responsible for executing the player's cycle logic.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerCycleTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.players.forEach { p ->
            val start = System.currentTimeMillis()
            p.cycle()
            /**
             * Log the time it takes for task to handle the player's cycle
             * logic.
             */
            val time = System.currentTimeMillis() - start
            service.playerTimes.merge(p.username, time) { _, oldTime -> oldTime + time }
        }
    }
}