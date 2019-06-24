package gg.rsmod.plugins.content.skills.firemaking

import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.*

/**
 * TODO: figure out what objects are used to represent the fire for each type of log
 * TODO: figure out the formula RS uses to calculate the burn time of a single log
 * TODO: figure out what formula RS uses for calculating the chance to successfully ignite a log
 *
 * This class is based upon the woodcutting skill by Tom.
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-06-23
 * @version 1.0
 */
object Firemaking {

    private const val ANIMATION = 733
    private const val IGNITING_LOGS_SOUND = 2597
    private const val BURNING_LOGS_SOUND = 2596
    private const val DEFAULT_ASHES = Items.ASHES

    const val DEFAULT_TINDERBOX = Items.TINDERBOX
    const val DEFAULT_FIRE = Objs.FIRE_26185

    data class Log(val type: LogType, val item: Int, val fire: Int)

    /**
     * Transforms the [item] into a [GroundItem] and then [lightLogOnGround].
     *
     * @param it the [QueueTask] instantiated by the firemaking plugin
     * @param item the log [Item] being burned
     * @param log the [LogType] of the log being burned
     * @param fireId the id of the fire [DynamicObject] associated with the log
     */
    suspend fun lightLog(it: QueueTask, item: Item, log: LogType, fireId: Int){

        val player = it.player

        if(!player.inventory.contains(item) || !canIgnite(player, log))
            return

        val groundItem = GroundItem(item, player.tile, player)

        player.inventory.remove(item)
        player.world.spawn(groundItem)

        lightLogOnGround(it, groundItem, log, fireId)
    }

    /**
     * Attempt to light a log on the ground to produce a fire.
     *
     * @param it the [QueueTask] instantiated by the firemaking plugin
     * @param groundItem the log [GroundItem] being burned
     * @param log the [LogType] of the log being burned
     * @param fireId the id of the fire [DynamicObject] associated with the log
     */
    suspend fun lightLogOnGround(it: QueueTask, groundItem: GroundItem, log: LogType, fireId: Int){

        val player = it.player

        if(!canIgnite(player, log))
            return

        player.filterableMessage("You attempt to light the logs.")

        while (true) {

            player.animate(ANIMATION)
            it.wait(2)
            player.playSound(IGNITING_LOGS_SOUND)
            it.wait(1)

            if (!canIgnite(player, log)) {
                player.animate(-1)
                break
            }

            if (attemptIgnition(player, log)) {

                player.animate(-1)

                val fire = DynamicObject(id = fireId, type = 10, rot = 0, tile = player.tile)
                val world = player.world
                world.queue {
                    world.remove(groundItem)
                    world.spawn(fire)
                    wait(log.burnTime.random())
                    world.remove(fire)
                    world.spawn(GroundItem(DEFAULT_ASHES, 1, fire.tile, player))
                }


                player.addXp(Skills.FIREMAKING, log.xp)
                player.filterableMessage("You light a fire.")
                player.playSound(BURNING_LOGS_SOUND)
                player.stepAway()
                it.wait(1)
                player.faceTile(fire.tile)
                break
            }
            it.wait(1)
        }
    }

    /**
     * Determine whether the [player] can ignite an [GroundItem] of type [log].
     *
     * @param player the [Player] lighting the [log]
     * @param log the [LogType] of which to check the [player]'s stats for
     * @return true if the [player] can ignite a [GroundItem] of type [log],
     *          false otherwise.
     */
    private fun canIgnite(player: Player, log: LogType): Boolean {

        if(player.getSkills().getMaxLevel(Skills.FIREMAKING) < log.level){
            player.message("You need a Firemaking level of ${log.level} to burn this log.")
            return false
        }

        if(!player.inventory.contains(DEFAULT_TINDERBOX)){
            player.message("You need a tinderbox to light a fire.")
            return false
        }

        if(player.world.getObject(player.tile, 10) != null){
            player.message("You can't light a fire here.")
            return false
        }

        return true
    }

    /**
     * Roll a dice for successfully igniting a log.
     *
     * @param player the [Player] attempting to ignite a log
     * @param log the [LogType] the type of log
     * @return true if the [player] successfully ignited a log,
     *          false otherwise.
     */
    private fun attemptIgnition(player: Player, log: LogType): Boolean{
        val level = player.getSkills().getCurrentLevel(Skills.FIREMAKING)
        val failureOdds = Math.random() * log.level
        val successOdds = Math.random() * ((level + 1 - log.level) * (1 + log.level * 0.01))
        return failureOdds < successOdds
    }
}