package gg.rsmod.plugins.content.skills.herblore.misc.sqirks

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.message

enum class SqirkJuices(val sqirks: Item, val juice: Int, val cookingXpAwarded: Double, val thieveBoost: Int, val runBoostPercent: Double) {
    WINTER(Item(Items.WINTER_SQIRK,5), Items.WINTER_SQIRKJUICE, 12.0, 0, .05),
    SPRING(Item(Items.SPRING_SQIRK, 4), Items.SPRING_SQIRKJUICE, 5.0, 1, .1),
    AUTUMN(Item(Items.AUTUMN_SQIRK, 3), Items.AUTUMN_SQIRKJUICE, 5.0, 2, .15),
    SUMMER(Item(Items.SUMMER_SQIRK, 2), Items.SUMMER_SQIRKJUICE, 5.0, 3, .2);

    fun squirsh(player: Player){
        if(player.inventory.remove(Items.BEER_GLASS).hasSucceeded()){
            if(player.inventory.remove(sqirks).hasSucceeded()){
                player.inventory.add(juice)
                player.addXp(Skills.COOKING, cookingXpAwarded)
            } else {
                player.message("You do not have the required sq'irks needed. You need ${sqirks.amount - player.inventory.getItemCount(sqirks.id)} more ${player.world.definitions.get(ItemDef::class.java, sqirks.id).name}.")
            }
        } else {
            player.message("You need a beer glass to make a sq'irk juice!")
        }
    }
}