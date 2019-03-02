package gg.rsmod.game.task

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService

/**
 * A [GameTask] responsible for executing the [gg.rsmod.game.plugin.Plugin.cycle].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PluginHandlerTask : GameTask {

    override fun execute(world: World, service: GameService) {
        var playerQueues = 0
        var npcQueues = 0
        var worldQueues = 0

        world.players.forEach { player ->
            player.queues.cycle()
            playerQueues += player.queues.size
        }

        world.npcs.forEach { npc ->
            npc.queues.cycle()
            npcQueues += npc.queues.size
        }

        world.queues.cycle()
        worldQueues = world.queues.size

        service.totalPlayerQueues = playerQueues
        service.totalNpcQueues = npcQueues
        service.totalWorldQueues = worldQueues
    }
}