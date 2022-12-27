package gg.rsmod.plugins.content.skills.fishing

import gg.rsmod.plugins.content.skills.fishing.action.Fishing
import gg.rsmod.plugins.content.skills.fishing.data.FishingSpot

/**
 * @author Vashmeed
 */

val spots = FishingSpot.values

spots.forEach { spot ->
    spot.fishingOption.forEach { option ->
        spot.npcIds.forEach { npc ->
            on_npc_option(npc = npc, option = option.option) {
                player.queue {
                    Fishing.fish(this, option)
                }
            }
        }
    }
}
