package gg.rsmod.plugins.content.skills.herblore

import gg.rsmod.plugins.content.skills.herblore.data.Grimy

Grimy.values().forEach { Grimy ->
    on_item_option(item = Grimy.grimyherb, option = "clean") {
        player.queue { Cleaning.Grimy(this, Grimy) }
    }
}
