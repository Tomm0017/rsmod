package gg.rsmod.game.task

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService

/**
 * A [GameTask] responsible for calling the [gg.rsmod.game.map.Map.pulse]
 * pulse.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class MapHandlerTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.maps.pulse(world)
    }
}