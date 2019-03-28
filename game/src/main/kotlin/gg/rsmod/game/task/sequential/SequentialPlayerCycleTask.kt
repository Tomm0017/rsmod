package gg.rsmod.game.task.sequential

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService
import gg.rsmod.game.task.GameTask

/**
 * A [GameTask] responsible for executing [gg.rsmod.game.model.entity.Player]
 * cycle logic, sequentially.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialPlayerCycleTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.players.forEach { p ->
            val start = System.currentTimeMillis()
            p.cycle()
            /*
             * Log the time it takes for task to handle the player's cycle
             * logic.
             */
            val time = System.currentTimeMillis() - start
            service.playerTimes.merge(p.username, time) { _, oldTime -> oldTime + time }
        }
    }
}