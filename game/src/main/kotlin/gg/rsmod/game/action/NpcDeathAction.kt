package gg.rsmod.game.action

import gg.rsmod.game.model.LockState
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.plugin.Plugin

/**
 * @author Tom <rspsmods@gmail.com>
 */
object NpcDeathAction {

    val deathPlugin: (Plugin) -> Unit = {
        val npc = it.ctx as Npc
        it.suspendable {
            death(it, npc)
        }
    }

    private suspend fun death(it: Plugin, npc: Npc) {
        // TODO: check if on osrs the npcs keep the same index after death,
        // this will expose whether they unregister an npc completely or make
        // them invisible upon death and if they respawn.

        val world = npc.world
        val deathAnimation = npc.combatDef.deathAnimation
        val deathDelay = npc.combatDef.deathDelay
        val respawnDelay = npc.combatDef.respawnDelay

        npc.lock = LockState.FULL
        if (deathAnimation != -1) {
            npc.facePawn(null)
            npc.animate(deathAnimation)
        }

        it.wait(deathDelay)
        npc.animate(-1)
        world.remove(npc)

        if (npc.respawns) {
            it.wait(respawnDelay)
            world.spawn(Npc(npc.id, npc.spawnTile, world))
        }
    }
}