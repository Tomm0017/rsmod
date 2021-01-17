package gg.rsmod.plugins.content.skills.herblore.misc

val FAILED_TEA = Items.RUINED_HERB_TEA
val GOOD_TEA = arrayOf(Items.GUTHIX_REST1, Items.GUTHIX_REST2, Items.GUTHIX_REST3, Items.GUTHIX_REST4)

val H_MIX = Items.HERB_TEA_MIX
val G_MIX = Items.HERB_TEA_MIX_4466
val M_MIX = Items.HERB_TEA_MIX_4468

val GUTHIX_BASE_MIXES = mapOf(
    Items.HARRALANDER to H_MIX,
    Items.GUAM_LEAF to G_MIX,
    Items.MARRENTILL to M_MIX
)

val HM_MIX = Items.HERB_TEA_MIX_4470
val HG_MIX = Items.HERB_TEA_MIX_4472

val GG_MIX = Items.HERB_TEA_MIX_4474
val GM_MIX = Items.HERB_TEA_MIX_4476

val HGM_MIX = Items.HERB_TEA_MIX_4478
val GGM_MIX = Items.HERB_TEA_MIX_4480
val GHG_MIX = Items.HERB_TEA_MIX_4482

/**
 * Step one starts from a [Items.CUP_OF_HOT_WATER] and each step can add
 * one of [Items.GUAM_LEAF], [Items.HARRALANDER], or [Items.MARRENTILL] ONLY!
 * Experience is awarded after adding each ingredient at a rate of: 14, 14.5, 15, 15.5
 * in each subsequent step
 */
GUTHIX_BASE_MIXES.forEach { (herb, mix) ->
    on_item_on_item(Items.CUP_OF_HOT_WATER, herb){
        if(player.comboItemReplace(Items.CUP_OF_HOT_WATER, mix, herb, 0, true)){
            player.addXp(Skills.HERBLORE, 14.0)
            player.message("You place the ${herb.getItemName(world.definitions, lowercase = true)} into the cup of hot water.")
        }
    }
}

fun bind_fail(mixItem: Int, herbItem: Int){
    on_item_on_item(mixItem, herbItem){
        if(player.comboItemReplace(mixItem, FAILED_TEA, herbItem, 0, true)){
            player.addXp(Skills.HERBLORE, 4.0)
            player.message("You place the ${herbItem.getItemName(world.definitions, true)} into the steamy mixture, it ruins the tea.")
        }
    }
}

fun bind_mixture(mixItem: Int, herbItem: Int, outMix: Int, xpGain: Double){
    on_item_on_item(mixItem, herbItem){
        if(player.comboItemReplace(mixItem, outMix, herbItem, 0, true)){
            player.addXp(Skills.HERBLORE, xpGain)
            player.message("You place the ${herbItem.getItemName(world.definitions, true)} into the steamy mixture.")
        }
    }
}

fun bind_complete(mixItem: Int, herbItem: Int){
    on_item_on_item(mixItem, herbItem){
        if(player.comboItemReplace(mixItem, GOOD_TEA[2], herbItem, 0, true)){
            player.addXp(Skills.HERBLORE, 15.5)
            player.message("You place the ${herbItem.getItemName(world.definitions, true)} into the steamy mixture and make Guthix Rest Tea.")
        }
    }
}

/**
 * Step two.
 */
bind_fail(H_MIX, Items.HARRALANDER)
bind_fail(M_MIX, Items.MARRENTILL)

bind_mixture(H_MIX, Items.GUAM_LEAF, HG_MIX, 14.5)
bind_mixture(H_MIX, Items.MARRENTILL, HM_MIX, 14.5)
bind_mixture(G_MIX, Items.GUAM_LEAF, GG_MIX, 14.5)
bind_mixture(G_MIX, Items.MARRENTILL, GM_MIX, 14.5)

/**
 * Step three.
 */
bind_fail(HG_MIX, Items.HARRALANDER)
bind_fail(HM_MIX, Items.HARRALANDER)
bind_fail(HM_MIX, Items.MARRENTILL)
bind_fail(GG_MIX, Items.GUAM_LEAF)
bind_fail(GM_MIX, Items.MARRENTILL)

bind_mixture(HG_MIX, Items.GUAM_LEAF, GHG_MIX, 15.0)
bind_mixture(HG_MIX, Items.MARRENTILL, HGM_MIX, 15.0)
bind_mixture(GG_MIX, Items.MARRENTILL, GGM_MIX, 15.0)

/**
 * Step four.
 */
bind_fail(GHG_MIX, Items.GUAM_LEAF)
bind_fail(GHG_MIX, Items.HARRALANDER)
bind_fail(HGM_MIX, Items.HARRALANDER)
bind_fail(HGM_MIX, Items.MARRENTILL)
bind_fail(GGM_MIX, Items.GUAM_LEAF)
bind_fail(GGM_MIX, Items.MARRENTILL)

bind_complete(GHG_MIX, Items.MARRENTILL)
bind_complete(HGM_MIX, Items.GUAM_LEAF)
bind_complete(GGM_MIX, Items.HARRALANDER)