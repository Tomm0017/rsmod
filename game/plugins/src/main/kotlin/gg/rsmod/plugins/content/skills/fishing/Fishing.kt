package gg.rsmod.plugins.content.skills.fishing

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.getVarp
import gg.rsmod.plugins.api.ext.interpolate
import gg.rsmod.plugins.api.ext.message
/*import gg.rsmod.plugins.content.modules.tutorialisland.TutorialIsland*/

class Fishing(val player: Player, val spot: FishingSpots) {
    suspend fun startFishing(it: QueueTask) {
        if(!canFish()) {
            player.animate(-1)
            return
        }

        while(true) {
            player.world.plugins.executeOnStartFishing(player, spot.spotEntityId)
            player.animate(spot.animation)
            it.wait(2)

            if(!canFish()) {
                player.animate(-1)
                break
            }

            val lvl = player.getSkills().getCurrentLevel(Skills.FISHING)

            val randomFish = getRandomFish()

            if(lvl.interpolate(minChance = 60, maxChance = 190, minLvl = randomFish.level, maxLvl = 99, cap = 255)) {
                player.world.plugins.executeOnCatchFish(player, spot.spotEntityId)
                player.message("You catch some fish.")

                /*if(player.getVarp(TutorialIsland.COMPLETION_VARP) >= 1000) {
                    player.message("You catch some fish.")
                }*/

                if(spot.baitId != -1) {
                    player.inventory.remove(spot.baitId, 1)
                }
                player.inventory.add(randomFish.fishItem, 1)
                player.addXp(Skills.FISHING, randomFish.xp)
            }
            it.wait(2)
        }
    }

    private fun getRandomFish(): Fish {
        return spot.getFish().filter { player.getSkills().getMaxLevel(Skills.FISHING) >= it.level }.random()
    }


    private fun canFish(): Boolean {
        if(!hasLevel()) {
            player.message("You need at least level ${minLevel()} fishing to catch fish here.")
            return false
        }

        if(!hasTool()) {
            player.message("You do not have the required tool to fish here.")
            return false
        }

        if(spot.baitId != -1) {
            if(!player.inventory.contains(spot.baitId)) {
                player.message("You do not have any bait to fish here.")
                return false
            }
        }

        if(player.inventory.isFull) {
            player.message("You do not have enough space to hold any more fish.")
            return false
        }

        // Tutorial island support
        /*if(player.getVarp(TutorialIsland.COMPLETION_VARP) == 50) {
            return false
        }*/

        return true
    }

    private fun hasLevel(): Boolean {
        var hasLevel = false
        spot.getFish().forEach { fish ->
            if(player.getSkills().getCurrentLevel(Skills.FISHING) >= fish.level) {
                hasLevel = true
            }
        }
        return hasLevel
    }

    private fun minLevel(): Int {
        var minLevel = 0
        spot.getFish().forEach { fish ->
            if(fish.level > minLevel) {
                minLevel = fish.level
            }
        }
        return minLevel
    }

    private fun hasTool(): Boolean {
        return player.inventory.contains(spot.toolId)
    }
}