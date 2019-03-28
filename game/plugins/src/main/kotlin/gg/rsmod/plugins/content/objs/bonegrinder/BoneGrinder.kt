package gg.rsmod.plugins.content.objs.bonegrinder

import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.hasItem
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.content.skills.prayer.Bones

/**
 * @author Misterbaho <MisterBaho#6447>
 */

object BoneGrinder{

    val BONE_GRINDER_BONES = AttributeKey<Int>()
    val BONE_GRINDER_STATUS = AttributeKey<Int>()

    fun status(it: QueueTask){
        val player = it.player
        val status = player.attr[BONE_GRINDER_STATUS]
        if(status == null){
            player.message("The grinder looks empty.")
        }
        if(status == 1){
            player.message("You have loaded bones in the grinder.")
        }
        if(status == 2){
            player.message("There are crushed bones in the bin.")
        }
    }

    suspend fun load(it: QueueTask, bones: Bones){
        val player = it.player
        val status = player.attr[BONE_GRINDER_STATUS]

        if(status != null) {
            player.message("You already have some bones in the hopper.")
            return
        }

        player.lock()
        player.animate(1649)
        it.wait(3)
        if (player.inventory.remove(item = bones.item).hasSucceeded()) {
            player.attr[BONE_GRINDER_BONES] = bones.crushedBone
            player.attr[BONE_GRINDER_STATUS] = 1
            player.message("You put the bones in the hopper.")
        }
        player.unlock()
    }

    suspend fun crush(it: QueueTask){
        val player = it.player
        val status = player.attr[BONE_GRINDER_STATUS]

        if(status != 1){
            player.message("There is nothing to be crushed.")
            return
        }

        player.lock()
        player.animate(1648)
        it.wait(3)
        player.attr[BONE_GRINDER_STATUS] = 2
        player.unlock()
    }

    suspend fun emptyBin(it: QueueTask){
        val player = it.player
        val status = player.attr[BONE_GRINDER_STATUS]
        if (status == null){
            player.message("You need to put some bones in the hopper and grind them first.")
            return
        }

        if (status == 1){
            player.message("You need to grind the bones by turning the grinder first.")
            return
        }

        if (status == 2){
            val crushedBones = player.attr[BONE_GRINDER_BONES]?.toInt() ?: 0
            if(!player.inventory.contains(Items.POT)){
                player.message("You need an empty pot to fill with the crushed bones.")
                return
            }
            player.lock()
            player.animate(1650)
            it.wait(3)
            if (player.inventory.remove(item = Items.POT).hasSucceeded()) {
                player.attr.remove(BONE_GRINDER_STATUS)
                player.attr.remove(BONE_GRINDER_BONES)
                player.inventory.add(crushedBones)
                player.message("You put the bones in the hopper.")
            }
            player.unlock()
        }
    }
}