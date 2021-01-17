package gg.rsmod.plugins.content.skills.herblore

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.skills.herblore.Herbs.Companion.AUTO_CLEAN
import kotlin.random.Random

class Herb(val dirty: Int, val clean: Int, val unfinished: Int, val minCleanLevel: Int, val xpOnClean: Double, val minApothLevel: Int) {
    fun clean(player: Player) {
        val lvlMessage = "You need level $minCleanLevel Herblore to clean the ${dirty.getItemName(player.world.definitions)}."
        when(AUTO_CLEAN){
            true -> {
                if(player.getSkills().getCurrentLevel(Skills.HERBLORE) < minCleanLevel)
                    player.message(lvlMessage)
                else
                    player.autoReplace(dirty, clean, slotAware = true, perform = {
                        player.animate(712) // cleaning herbs technically does not have animation
                        player.playSound(Random.nextInt(3920, 3923), 1, 0)
                    }, success = {
                        player.addXp(Skills.HERBLORE, xpOnClean)
                    })
            }
            false -> {
                if(player.replaceItemWithSkillRequirement(dirty, clean, Skills.HERBLORE, minCleanLevel,
                                slot = player.getInteractingItemSlot(),minLvlMessage = lvlMessage)){
                    player.animate(712) // cleaning herbs technically does not have animation
                    player.playSound(Random.nextInt(3920, 3923), 1, 0)
                    player.addXp(Skills.HERBLORE, xpOnClean)
                }
            }
        }
    }

    fun apoth(player: Player) {
        if(player.replaceItemAndRemoveAnotherWithSkillRequirement(Items.VIAL_OF_WATER, unfinished, clean.toItem(), Skills.HERBLORE, minApothLevel)){
            player.animate(id = 363, delay = 0)
            player.playSound(2608, 1, 0)
        }
    }
}

enum class Herbs(val herb: Herb) {
    GUAM(Herb(Items.GRIMY_GUAM_LEAF, Items.GUAM_LEAF, Items.GUAM_POTION_UNF, 3, 2.5, 3)),
    ROGUES_PURSE(Herb(Items.GRIMY_ROGUES_PURSE, Items.ROGUES_PURSE, Items.UNFINISHED_POTION_4840, 3, 2.5, 8)),
    SNAKE_WEED(Herb(Items.GRIMY_SNAKE_WEED, Items.SNAKE_WEED, Items.SNAKEWEED_MIXTURE, 3, 2.5, 45)),
    MARRENTILL(Herb(Items.GRIMY_MARRENTILL, Items.MARRENTILL, Items.MARRENTILL_POTION_UNF, 5, 3.8, 5)),
    TARROMIN(Herb(Items.GRIMY_TARROMIN, Items.TARROMIN, Items.TARROMIN_POTION_UNF, 11, 5.0, 12)),
    HARRALANDER(Herb(Items.GRIMY_HARRALANDER, Items.HARRALANDER, Items.HARRALANDER_POTION_UNF, 20, 6.3, 22)),
    RANARR_WEED(Herb(Items.GRIMY_RANARR_WEED, Items.RANARR_WEED, Items.RANARR_POTION_UNF, 25, 7.5, 30)),
    TOADFLAX(Herb(Items.GRIMY_TOADFLAX, Items.TOADFLAX, Items.TOADFLAX_POTION_UNF, 30, 8.0, 34)),
    IRIT(Herb(Items.GRIMY_IRIT_LEAF, Items.IRIT_LEAF, Items.IRIT_POTION_UNF, 40, 8.8, 45)),
    AVANTOE(Herb(Items.GRIMY_AVANTOE, Items.AVANTOE, Items.AVANTOE_POTION_UNF, 48, 10.0, 50)),
    KWUARM(Herb(Items.GRIMY_KWUARM, Items.KWUARM, Items.KWUARM_POTION_UNF, 54, 11.3, 55)),
    SNAPDRAGON(Herb(Items.GRIMY_SNAPDRAGON, Items.SNAPDRAGON, Items.SNAPDRAGON_POTION_UNF, 59, 11.8, 63)),
    CADANTINE(Herb(Items.GRIMY_CADANTINE, Items.CADANTINE, Items.CADANTINE_POTION_UNF, 65, 12.5, 66)),
    LANTADYME(Herb(Items.GRIMY_LANTADYME, Items.LANTADYME, Items.LANTADYME_POTION_UNF, 67, 13.1, 69)),
    DWARF_WEED(Herb(Items.GRIMY_DWARF_WEED, Items.DWARF_WEED, Items.DWARF_WEED_POTION_UNF, 70, 13.8, 72)),
    TORSTOL(Herb(Items.GRIMY_TORSTOL, Items.TORSTOL, Items.TORSTOL_POTION_UNF, 75, 15.0, 78));

    companion object {
        val values = values()

        var AUTO_CLEAN = true
    }
}