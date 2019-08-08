package gg.rsmod.plugins.content.skills.prayer

import gg.rsmod.game.model.Tile
//import gg.rsmod.game.model.TileGraphic
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.filterableMessage
import gg.rsmod.plugins.api.ext.message
import gg.rsmod.plugins.api.ext.options
import gg.rsmod.plugins.api.ext.player

object BoneBurying {

    suspend fun bury(task: QueueTask, bone: Bone) {
        val player = task.player

        player.lock()
        player.inventory.remove(item = Item(bone.item))
        player.animate(827)
        player.filterableMessage("You dig a hole in the ground...")
        task.wait(1)
        player.filterableMessage("You bury the bones.")
        player.unlock()

        if (bone.item == 11943 /*&& player.tile.inArea(3172, 3799, 3232, 3857)*/) { // Lava drag isle check
            player.addXp(Skills.PRAYER, bone.xp * 4)
        } else {
            player.addXp(Skills.PRAYER, bone.xp)
        }
    }

    suspend fun bonesOnAltar(task: QueueTask, obj: GameObject, bone: Bone) {
        val player = task.player

        while (player.inventory.contains(bone.item)) {
            if (player.inventory.remove(Item(bone.item), true).hasFailed()) {
                player.filterableMessage("You have ran out of bones.")
                return
            }

            player.animate(896)
            //player.world.spawn(TileGraphic(obj.tile, 624, 0, 0))

            if (player.world.getObject(Tile(3095, 3506), 10) != null && player.world.getObject(Tile(3098, 3506), 10) != null) {
                player.filterableMessage("The gods are very pleased with your offerings.")
                player.addXp(Skills.PRAYER, bone.xp * 3)
            } else {
                player.filterableMessage("The gods are pleased with your offerings.")
                player.addXp(Skills.PRAYER, bone.xp * 2)
            }

            task.wait(3)
        }
    }
}