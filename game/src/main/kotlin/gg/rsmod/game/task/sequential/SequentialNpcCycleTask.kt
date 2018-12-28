package gg.rsmod.game.task.sequential

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService
import gg.rsmod.game.task.GameTask

/**
 * A [GameTask] responsible for executing [gg.rsmod.game.model.entity.Npc]
 * cycle logic, sequentially.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialNpcCycleTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.npcs.forEach { n ->
            n.cycle()
        }
    }
}