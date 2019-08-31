package gg.rsmod.game.action

import gg.rsmod.game.fs.def.AnimDef
import gg.rsmod.game.model.LockState
import gg.rsmod.game.model.attr.KILLER_ATTR
import gg.rsmod.game.model.droptable.createItem
import gg.rsmod.game.model.droptable.toGroundItem
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.game.model.queue.TaskPriority
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.service.log.LoggerService
import java.lang.ref.WeakReference
import kotlin.random.Random

/**
 * This class is responsible for handling npc death events.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object NpcDeathAction {

    val deathPlugin: Plugin.() -> Unit = {
        val npc = ctx as Npc

        npc.interruptQueues()
        npc.stopMovement()
        npc.lock()

        npc.queue(TaskPriority.STRONG) {
            death(npc)
        }
    }

    private suspend fun QueueTask.death(npc: Npc) {
        val world = npc.world
        val deathAnimation = npc.combatDef.deathAnimation
        val respawnDelay = npc.combatDef.respawnDelay

        npc.damageMap.getMostDamage()?.let { killer ->
            if (killer is Player) {
                world.getService(LoggerService::class.java, searchSubclasses = true)?.logNpcKill(killer, npc)
            }
            npc.attr[KILLER_ATTR] = WeakReference(killer)
        }

        world.plugins.executeNpcPreDeath(npc)

        npc.resetFacePawn()

        deathAnimation.forEach { anim ->
            val def = npc.world.definitions.get(AnimDef::class.java, anim)
            npc.animate(def.id)
            wait(def.cycleLength + 1)
        }

        npc.animate(-1)

        world.plugins.executeNpcDeath(npc)

        /**Thanks Nbness2 for making this work properly*/
        npc.dropTables.always_table.items!!.forEach { always_item ->
            world.spawn(always_item.createItem().toGroundItem(npc.tile))
        }

        var _table = npc.dropTables.common_table
        var _rolls = npc.dropTables.rolls
        var _roll_count = 0

        while(_roll_count < _rolls) {
            var r: Double = Random.nextDouble(0.0, 100.0)
            if(r <= npc.dropTables.veryrare_table.percentage && npc.dropTables.veryrare_table.items!!.isNotEmpty()) {
                _table = npc.dropTables.veryrare_table
            } else if(r <= npc.dropTables.rare_table.percentage && npc.dropTables.rare_table.items!!.isNotEmpty()) {
                _table = npc.dropTables.rare_table
            } else if(r <= npc.dropTables.uncommon_table.percentage && npc.dropTables.uncommon_table.items!!.isNotEmpty()) {
                _table = npc.dropTables.uncommon_table
            } else if(r <= npc.dropTables.common_table.percentage && npc.dropTables.common_table.items!!.isNotEmpty()) {
                _table = npc.dropTables.common_table
            }

            if(npc.dropTables.common_table.items!!.size == 0 && npc.dropTables.uncommon_table.items!!.size == 0 && npc.dropTables.rare_table.items!!.size == 0 && npc.dropTables.veryrare_table.items!!.size == 0) {
                break
            }

            if(_table.items!!.size > 0) {
                val _item_random = Random.nextInt(0, _table.items!!.size)
                val _item = _table.items!![_item_random]
                var _item_quanity = 0

                if(_item.quanityMin == _item.quanityMax) {
                    _item_quanity = _item.quanityMax
                } else {
                    _item_quanity = Random.nextInt(_item.quanityMin, _item.quanityMax)
                }

                world.spawn(item = GroundItem(_item.itemId, _item_quanity, npc.tile))
                _roll_count++
            }
        }

        if (npc.respawns) {
            npc.invisible = true
            npc.reset()
            wait(respawnDelay)
            npc.invisible = false
            world.plugins.executeNpcSpawn(npc)
        } else {
            world.remove(npc)
        }
    }

    private fun Npc.reset() {
        lock = LockState.NONE
        tile = spawnTile
        setTransmogId(-1)

        attr.clear()
        timers.clear()
        world.setNpcDefaults(this)
    }
}
