package gg.rsmod.game.task

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService

/**
 * A [GameTask] responsible for going over all the active
 * [gg.rsmod.game.model.queue.QueueTask]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class QueueHandlerTask : GameTask {

    override fun execute(world: World, service: GameService) {
        var playerQueues = 0
        var npcQueues = 0

        world.players.forEach { player ->
            player.queues.cycle()
            playerQueues += player.queues.size
        }

        world.npcs.forEach { npc ->
            npc.queues.cycle()
            npcQueues += npc.queues.size
        }

        val worldQueues: Int = world.queues.size
        world.queues.cycle()

        service.totalPlayerQueues = playerQueues
        service.totalNpcQueues = npcQueues
        service.totalWorldQueues = worldQueues
    }
}