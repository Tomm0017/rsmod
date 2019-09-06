package gg.rsmod.plugins.content.skills.herblore

import gg.rsmod.plugins.content.skills.herblore.data.Grimy
import gg.rsmod.plugins.content.skills.herblore.data.Potions


Grimy.values().forEach { Grimy ->
    on_item_option(item = Grimy.grimyherb, option = "clean") {
        player.queue { Cleaning.Grimy(this, Grimy) }
    }
}

Potions.values().forEach { Potions ->
    on_item_on_item(item1 = Items.VIAL_OF_WATER, item2 = Potions.firstingreident) {
        player.queue { Potionmaking.unfinished(this, Potions) }
    }
    on_item_on_item(item1 = Potions.unfinished, item2 = Potions.secondingreident) {
        player.queue { Potionmaking.finished(this, Potions) }
    }
}
