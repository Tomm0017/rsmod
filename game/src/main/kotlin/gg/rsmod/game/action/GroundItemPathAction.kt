package gg.rsmod.game.action

import gg.rsmod.game.message.impl.SetMapFlagMessage
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.attr.GROUNDITEM_PICKUP_TRANSACTION
import gg.rsmod.game.model.attr.INTERACTING_GROUNDITEM_ATTR
import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.game.model.queue.TaskPriority
import gg.rsmod.game.plugin.Plugin
import java.lang.ref.WeakReference

/**
 * This class is responsible for moving towards a [GroundItem].
 *
 * @author Tom <rspsmods@gmail.com>
 */
object GroundItemPathAction {

    val walkPlugin: Plugin.() -> Unit = {
        val p = ctx as Player
        val item = p.attr[INTERACTING_GROUNDITEM_ATTR]!!.get()!!
        val opt = p.attr[INTERACTING_OPT_ATTR]!!

        if (p.tile.sameAs(item.tile)) {
            handleAction(p, item, opt)
        } else {
            p.walkTo(item.tile, MovementQueue.StepType.NORMAL)
            p.queue(TaskPriority.STANDARD) {
                terminateAction = {
                    p.stopMovement()
                    p.write(SetMapFlagMessage(255, 255))
                }
                awaitArrival(item, opt)
            }
        }
    }

    private suspend fun QueueTask.awaitArrival(item: GroundItem, opt: Int) {
        val p = ctx as Player
        val destination = p.movementQueue.peekLast()
        if (destination == null) {
            p.message(Entity.YOU_CANT_REACH_THAT)
            return
        }
        while (true) {
            if (!p.tile.sameAs(destination)) {
                wait(1)
                continue
            }
            // TODO: check if player can grab the item by leaning over
            if (p.tile.sameAs(item.tile)) {
                handleAction(p, item, opt)
            } else {
                p.message(Entity.YOU_CANT_REACH_THAT)
            }
            break
        }
    }

    private fun handleAction(p: Player, item: GroundItem, opt: Int) {
        if (!p.world.isSpawned(item)) {
            return
        }
        if (opt == 3) {
            // NOTE(Tom): we may want to make the assureFullInsertion flag false and
            // allow the world to remove some of the ground item instead of all of it.
            val add = p.inventory.add(item = item.item, amount = item.amount, assureFullInsertion = true)
            if (add.completed == 0) {
                p.message("You don't have enough inventory space to hold that item.")
                return
            }

            p.world.remove(item)

            p.attr[GROUNDITEM_PICKUP_TRANSACTION] = WeakReference(add)
            p.world.plugins.executeGlobalGroundItemPickUp(p)
        } else {
            p.attr[INTERACTING_GROUNDITEM_ATTR] = WeakReference(item)
            p.world.plugins.executeGroundItem(p, item.item, opt)
        }
    }
}