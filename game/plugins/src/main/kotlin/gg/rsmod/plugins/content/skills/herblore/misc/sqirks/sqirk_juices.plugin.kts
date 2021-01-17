package gg.rsmod.plugins.content.skills.herblore.misc.sqirks

import gg.rsmod.plugins.content.skills.herblore.pestle.Crushables.Companion.PaM

SqirkJuices.values().forEach { juice ->
    on_item_on_item(PaM, juice.sqirks.id) {
        juice.squirsh(player)
    }
    on_item_on_item(PaM, juice.juice){
        player.nothingMessage()
    }

    on_item_on_item(juice.sqirks.id, Items.VIAL){
        player.nothingMessage()
    }
    on_item_on_item(juice.sqirks.id, Items.VIAL_OF_WATER){
        player.nothingMessage()
    }
}
