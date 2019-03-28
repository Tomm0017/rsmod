package gg.rsmod.plugins.content.skills.prayer

import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*

/**
 * @author Misterbaho <MisterBaho#6447>
 */

object Prayer {

    private const val BURY_ANIM = 827

    private const val PRAY_ANIM = 1651

    private const val WAITING_CYCLE = 3

    private const val BURY_SOUND = 2738

    suspend fun burying(it: QueueTask, bones: Bones) {
        val player = it.player

        player.lock()
        player.filterableMessage("You dig a hole in the ground...")
        val inventorySlot = player.getInteractingItemSlot()
        if (player.inventory.remove(item = bones.item, beginSlot = inventorySlot).hasSucceeded()) {
            player.addXp(Skills.PRAYER, bones.xp)
        }
        player.animate(BURY_ANIM)
        player.playSound(BURY_SOUND)
        it.wait(WAITING_CYCLE)
        player.filterableMessage("You bury the bones.")

        player.unlock()
    }

    suspend fun worship(it: QueueTask) {
        val player = it.player
        val bones = Bones

        bones.values.forEach { crushedBones ->
            if (player.inventory.containsAny(crushedBones.crushedBone)) {
                if (player.inventory.containsAny(Items.BUCKET_OF_SLIME)) {
                    player.lock()
                    player.animate(PRAY_ANIM)
                    it.wait(WAITING_CYCLE)
                    if (player.inventory.remove(item = crushedBones.crushedBone).hasSucceeded().and
                            (player.inventory.remove(item = Items.BUCKET_OF_SLIME).hasSucceeded())) {
                        player.addXp(Skills.PRAYER, crushedBones.xp * 4)
                        player.inventory.add(Items.BUCKET)
                        player.inventory.add(Items.POT)
                    }
                    player.filterableMessage("You put some ectoplasm and bonemeal into the ectofuntus, and worship it.")
                    player.unlock()
                    return
                }
                player.message("You need a bucket of slime before you can worship the ectofuntus.")
                return
            }
        }
        player.message("You need a bucket of slime and bonemeal before you can worship the ectofuntus.")
        return
    }

    suspend fun chaosAltar(it: QueueTask, bones: Bones) {
        val player = it.player
        val inventory = player.inventory
        var chance = 1

        while (inventory.containsAny(bones.item)) {
            it.wait(2)
            if (it.pawn.world.random(chance) == 1) {
                player.addXp(Skills.PRAYER, bones.xp * 3.5)
                chance.times(2).plus(1)
                player.message("The Dark Lord spares you sacrifice but still rewards you for your efforts.")
            } else {
                if (player.inventory.remove(item = bones.item).hasSucceeded()) {
                    player.addXp(Skills.PRAYER, bones.xp * 3.5)
                }
                chance = 1
            }
            player.animate(713)
            player.graphic(id = 624,height = 1,delay =1)
            it.wait(1)
        }
    }

}