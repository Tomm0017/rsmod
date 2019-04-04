package gg.rsmod.game.task

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService

/**
 * A [GameTask] responsible for handling all incoming
 * [gg.rsmod.game.message.Message]s, sequentially.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class MessageHandlerTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.players.forEach { p ->
            val start = System.currentTimeMillis()
            p.handleMessages()
            /*
             * Log the time it takes for the task to handle all the player's
             * incoming messages.
             */
            val time = System.currentTimeMillis() - start
            service.playerTimes.merge(p.username, time) { _, oldTime -> oldTime + time }
        }
    }
}