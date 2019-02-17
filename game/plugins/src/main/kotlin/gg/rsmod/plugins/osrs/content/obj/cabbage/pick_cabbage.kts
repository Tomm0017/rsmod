package gg.rsmod.plugins.osrs.content.obj.cabbage

import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.plugins.osrs.api.cfg.Items
import gg.rsmod.plugins.osrs.api.ext.getInteractingGameObj
import gg.rsmod.plugins.osrs.api.ext.player

val RESPAWN_DELAY = 75

on_obj_option(obj = 1161, option = "pick", lineOfSightDistance = 0) {
    val player = it.player()
    val obj = it.getInteractingGameObj()

    it.suspendable {
        val route = player.walkTo(it, obj.tile, MovementQueue.StepType.NORMAL)
        if (route.success) {
            if (player.inventory.isFull()) {
                player.message("You don't have room for this cabbage.")
                return@suspendable
            }
            if (obj.isSpawned(player.world)) {
                val item = if (player.world.percentChance(5.0)) Items.CABBAGE_SEED else Items.CABBAGE
                player.animate(827)
                player.inventory.add(item = item)
                player.world.remove(obj)
                player.world.executePlugin(obj) {
                    it.suspendable {
                        it.wait(RESPAWN_DELAY)
                        player.world.spawn(DynamicObject(obj))
                    }
                }
            }
        } else {
            player.message(Entity.YOU_CANT_REACH_THAT)
        }
    }
}