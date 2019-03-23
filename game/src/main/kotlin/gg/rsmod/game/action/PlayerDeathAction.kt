package gg.rsmod.game.action

import gg.rsmod.game.fs.def.AnimDef
import gg.rsmod.game.model.attr.KILLER_ATTR
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.game.model.queue.TaskPriority
import gg.rsmod.game.plugin.Plugin
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PlayerDeathAction {

    private const val DEATH_ANIMATION = 836

    val deathPlugin: Plugin.() -> Unit = {
        val player = ctx as Player
        player.interruptQueues()
        player.lock()
        player.queue(TaskPriority.STRONG) {
            death(player)
        }
    }

    private suspend fun QueueTask.death(player: Player) {
        val world = player.world
        val deathAnim = world.definitions.get(AnimDef::class.java, DEATH_ANIMATION)
        val instancedMap = world.instanceAllocator.getMap(player.tile)

        player.damageMap.getMostDamage()?.let { killer ->
            player.attr[KILLER_ATTR] = WeakReference(killer)
        }

        world.plugins.executePlayerPreDeath(player)

        wait(2)
        player.animate(deathAnim.id)
        wait(deathAnim.cycleLength + 1)
        player.getSkills().restoreAll()
        player.animate(-1)
        if (instancedMap == null) {
            // Note: maybe add a player attribute for death locations
            player.teleport(player.world.gameContext.home)
        } else {
            player.teleport(instancedMap.exitTile)
            world.instanceAllocator.death(player)
        }
        player.message("Oh dear, you are dead!")
        player.unlock()

        world.plugins.executePlayerDeath(player)
    }
}