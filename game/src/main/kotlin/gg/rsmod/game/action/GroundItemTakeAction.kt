package gg.rsmod.game.action

import gg.rsmod.game.model.INTERACTING_GROUNDITEM_ATTR
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin

/**
 * This class is responsible for moving towards a [GroundItem] and handling
 * the pick-up logic.
 * 
 * @author Tom <rspsmods@gmail.com>
 */
object GroundItemTakeAction {

    val walkPlugin: (Plugin) -> Unit = {
        val p = it.ctx as Player
        val item = p.attr[INTERACTING_GROUNDITEM_ATTR]!!.get()!!
        if (p.tile.sameAs(item.tile)) {
            handleAction(p, item)
        } else {
            p.walkTo(item.tile, MovementQueue.StepType.NORMAL)
            it.suspendable {
                awaitArrival(it, item)
            }
        }
    }

    private suspend fun awaitArrival(it: Plugin, item: GroundItem) {
        val p = it.ctx as Player
        val destination = p.movementQueue.peekLast()
        if (destination == null) {
            p.message(Entity.YOU_CANT_REACH_THAT)
            return
        }
        while (true) {
            if (!p.tile.sameAs(destination)) {
                it.wait(1)
                continue
            }
            // TODO: check if player can grab the item by leaning over
            if (p.tile.sameAs(item.tile)) {
                handleAction(p, item)
            } else {
                p.message(Entity.YOU_CANT_REACH_THAT)
            }
            break
        }
    }

    private fun handleAction(p: Player, item: GroundItem) {
        if (!p.world.isSpawned(item)) {
            return
        }
        // NOTE(Tom): we may want to make the assureFullInsertion flag false and
        // allow the world to remove some of the ground item instead of all of it.
        val add = p.inventory.add(id = item.item, amount = item.amount, assureFullInsertion = true)
        if (add.completed == 0) {
            p.message("You don't have enough inventory space to hold that item.")
            return
        }

        p.world.remove(item)
    }
}