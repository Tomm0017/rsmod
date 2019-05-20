package gg.rsmod.game.task

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService

/**
 * A [GameTask] responsible for handling entity removal from the [World] when
 * appropriate.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class WorldRemoveTask : GameTask {

    override fun execute(world: World, service: GameService) {
        for (i in 0 until world.npcs.capacity) {
            val npc = world.npcs[i] ?: continue
            if (npc.owner?.isOnline == false) {
                world.remove(npc)
            }
        }
    }
}