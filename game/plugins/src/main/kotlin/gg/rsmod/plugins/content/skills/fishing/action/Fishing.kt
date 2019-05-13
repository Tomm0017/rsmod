package gg.rsmod.plugins.content.skills.fishing.action

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.filterableMessage
import gg.rsmod.plugins.api.ext.interpolate
import gg.rsmod.plugins.api.ext.playSound
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.content.skills.fishing.data.Fish
import gg.rsmod.plugins.content.skills.fishing.data.FishingOption
import kotlin.random.Random

/**
 * @author Vashmeed
 */
object Fishing {
    suspend fun fish(it: QueueTask, option: FishingOption) {
        val p = it.player

        if (!canFish(p, option)) {
            return
        }

        while (true) {
            p.animate(option.animationId)
            it.wait(2)

            if (!canFish(p, option)) {
                p.animate(-1)
                break
            }

            val level = p.getSkills().getCurrentLevel(Skills.FISHING)
            if (level.interpolate(minChance = 60, maxChance = 190, minLvl = 1, maxLvl = 99, cap = 255)) {
                p.filterableMessage("You catch some fish.")
                p.playSound(3600)
                if (option.bait != null)
                    p.inventory.remove(option.bait)
                val node = reward(p, option)

                p.inventory.add(node.item)
                p.addXp(Skills.FISHING, node.experience)
            }
            it.wait(2)
        }
    }

    private fun reward(p: Player, option: FishingOption): Fish {
        if (option.equals(FishingOption.BIG_NET)) {
            when (Random.nextInt(0, 100)) {
                0 -> Fish.OYSTER
                50 -> Fish.CASKET
                90 -> Fish.SEAWEED
            }
        }
        if (option.equals(FishingOption.LURE) && p.inventory.contains(10087)) {
            return Fish.RAINBOW_FISH
        }
        return option.fishType.filter { p.getSkills().getMaxLevel(Skills.FISHING) >= it.levelReq }.random()
    }

    private fun canFish(p: Player, npc: FishingOption): Boolean {
        val tool = FishingOption.values.firstOrNull { p.getSkills().getMaxLevel(Skills.FISHING) >= it.levelReq && (p.equipment.contains(npc.toolId) || p.inventory.contains(npc.toolId)) }
        if (tool == null) {
            p.message("You need a ${p.world.definitions.get(ItemDef::class.java, npc.toolId).name} to fish here.")
            return false
        }

        if (npc.bait != null) {
            val bait = FishingOption.values.firstOrNull { (p.inventory.contains(npc.bait)) }
            if (bait == null) {
                p.message("You need ${p.world.definitions.get(ItemDef::class.java, npc.bait).name} to bait this fish.")
                return false
            }
        }
        if (p.getSkills().getMaxLevel(Skills.FISHING) < npc.levelReq) {
            p.message("You need a Fishing level of ${npc.levelReq} to fish here.")
            return false
        }

        if (p.inventory.isFull) {
            p.message("Your inventory is too full to hold any more fish.")
            return false
        }

        return true
    }
}