package gg.rsmod.game.task.sequential

import gg.rsmod.game.model.World
import gg.rsmod.game.service.GameService
import gg.rsmod.game.sync.task.*
import gg.rsmod.game.task.GameTask

/**
 * A [GameTask] that is responsible for sending [gg.rsmod.game.model.entity.Pawn]
 * data to [gg.rsmod.game.model.entity.Pawn]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialSynchronizationTask : GameTask {

    override fun execute(world: World, service: GameService) {
        val worldPlayers = world.players
        val worldNpcs = world.npcs
        val rawNpcs = world.npcs.entries

        worldPlayers.forEach { p ->
            PlayerPreSynchronizationTask(p).run()
        }

        worldNpcs.forEach { n ->
            NpcPreSynchronizationTask(n).run()
        }

        worldPlayers.forEach { p ->
            /*
             * Non-human [gg.rsmod.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (p.getType().isHumanControlled() && p.initiated) {
                PlayerSynchronizationTask(p).run()
            }
        }

        worldPlayers.forEach { p ->
            /*
             * Non-human [gg.rsmod.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (p.getType().isHumanControlled() && p.initiated) {
                NpcSynchronizationTask(p, rawNpcs).run()
            }
        }

        worldPlayers.forEach { p ->
            PlayerPostSynchronizationTask(p).run()
        }

        worldNpcs.forEach { n ->
            NpcPostSynchronizationTask(n).run()
        }
    }
}