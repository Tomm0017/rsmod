package gg.rsmod.plugins.content.mechanics.water

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*

class WaterContainer(val unfilled: Int, val filled: Int) {
    fun fill(player: Player, message:String) {
        // always succeeds
        player.autoReplace(unfilled, filled, growingDelay = false, slotAware = true, perform = {
            player.queue {
                player.animate(832)
                player.playSound(2609, 1, 0)
            }
        }, success = { player.message(message) })
    }

    fun empty(player: Player) {
        if(player.replaceItemInSlot(filled, unfilled, player.getInteractingItemSlot())){
            player.queue {
                // only some make sounds when emptying
                if(unfilled.getItemName(player.world.definitions).contains(Regex("Bowl|Bucket|Jug"))){
                    player.playSound(2401, 1, 0)
                }
                player.message("You empty the contents of the ${unfilled.getItemName(player.world.definitions, lowercase = true)} on the floor.")
            }
        }
    }
}

enum class WaterContainers(val container: WaterContainer) {
    BOWL(WaterContainer(Items.BOWL, Items.BOWL_OF_WATER)),
    BUCKET(WaterContainer(Items.BUCKET, Items.BUCKET_OF_WATER)),
    CAN(WaterContainer(Items.WATERING_CAN, Items.WATERING_CAN8)),
    CUP(WaterContainer(Items.EMPTY_CUP, Items.CUP_OF_WATER)),
    JUG(WaterContainer(Items.JUG, Items.JUG_OF_WATER)),
    VIAL(WaterContainer(Items.VIAL, Items.VIAL_OF_WATER)),
    WATERSKIN(WaterContainer(Items.WATERSKIN0, Items.WATERSKIN4))
}