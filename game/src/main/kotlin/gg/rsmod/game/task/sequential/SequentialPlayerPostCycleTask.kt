package gg.rsmod.game.task.sequential

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService
import gg.rsmod.game.task.GameTask

/**
 * A [GameTask] responsible for executing [gg.rsmod.game.model.entity.Pawn]
 * "post" cycle logic, sequentially. Post cycle means that the this task
 * will be handled near the end of the cycle, after the synchronization
 * tasks.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialPlayerPostCycleTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.players.forEach { p ->
            p.postCycle()
        }
    }
}