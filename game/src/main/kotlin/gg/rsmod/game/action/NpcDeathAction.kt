package gg.rsmod.game.action

import gg.rsmod.game.fs.def.AnimDef
import gg.rsmod.game.model.LockState
import gg.rsmod.game.model.entity.Npc
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
            death(this, npc)
        }
    }

    private suspend fun death(it: Plugin, npc: Npc) {
        val world = npc.world
        val deathAnimation = npc.combatDef.deathAnimation
        val respawnDelay = npc.combatDef.respawnDelay

        npc.lock = LockState.FULL

        npc.facePawn(null)
        deathAnimation.forEach { anim ->
            val def = npc.world.definitions.get(AnimDef::class.java, anim)
            npc.animate(def.id)
            it.wait(def.cycleLength)
        }
        npc.animate(-1)
        world.remove(npc)

        if (npc.respawns) {
            it.wait(respawnDelay)
            world.spawn(Npc(npc.id, npc.spawnTile, world))
        }
    }
}