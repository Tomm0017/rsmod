package gg.rsmod.game.action

import gg.rsmod.game.fs.def.AnimDef
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.game.plugin.Plugin

/**
 * This class is responsible for handling npc death events.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object NpcDeathAction {

    val deathPlugin: Plugin.() -> Unit = {
        val npc = ctx as Npc
        npc.queue {
            death(npc)
        }
    }

    private suspend fun QueueTask.death(npc: Npc) {
        val world = npc.world
        val deathAnimation = npc.combatDef.deathAnimation
        val respawnDelay = npc.combatDef.respawnDelay

        npc.lock()
        npc.facePawn(null)

        deathAnimation.forEach { anim ->
            val def = npc.world.definitions.get(AnimDef::class.java, anim)
            npc.animate(def.id)
            wait(def.cycleLength)
        }

        npc.animate(-1)
        world.remove(npc)

        if (npc.respawns) {
            wait(respawnDelay)
            world.spawn(Npc(npc.id, npc.spawnTile, world))
        }
    }
}