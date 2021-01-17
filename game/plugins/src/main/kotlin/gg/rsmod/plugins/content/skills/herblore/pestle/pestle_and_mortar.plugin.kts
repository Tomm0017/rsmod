package gg.rsmod.plugins.content.skills.herblore.pestle

import gg.rsmod.plugins.content.skills.herblore.pestle.Crushables.Companion.PaM
import gg.rsmod.plugins.content.skills.herblore.pestle.Crushables.*
import gg.rsmod.plugins.content.skills.herblore.Herbs
import gg.rsmod.plugins.content.skills.herblore.pots.Pots

/**
 * Pestle and mortar registers for each [Crushables]
 */
values().forEach crushing@{ supply ->
    on_item_on_item(PaM, supply.itemIn){
        supply.crush(player)
    }
    if(supply== KARAMBWAN_COOKED_POISONED || supply== KARAMBWAN_COOKED2) return@crushing
    on_item_on_item(PaM, supply.itemOut){
        player.nothingMessage()
    }
}

on_item_on_item(PaM, -1){
    player.message("Item not crushable!")
}

/**
 * [Crushables] on other [Crushables], [Herbs], [Pots] all do nothing
 */
values().forEach { supply ->
    on_item_on_item(supply.itemIn, supply.itemIn) {
        player.nothingMessage()
    }
    on_item_on_item(supply.itemIn, supply.itemOut) {
        player.nothingMessage()
    }
    if(supply!= KARAMBWAN_COOKED_POISONED && supply!= KARAMBWAN_COOKED2){
        on_item_on_item(supply.itemOut, supply.itemOut) {
            player.nothingMessage()
        }
    }
}

val NOTHING_BONES = arrayOf(Items.BONES)

NOTHING_BONES.forEach { bones ->
    on_item_on_item(PaM, bones){
        player.nothingMessage()
    }
}