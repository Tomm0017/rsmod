package gg.rsmod.plugins.content.skills.crafting

import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.skills.crafting.data.Gems
import gg.rsmod.plugins.content.skills.crafting.data.Spin

val spinningObj = 14889

Gems.values().forEach { gems ->
    on_item_on_item(Items.CHISEL, gems.uncutGem) {
        player.queue { Cutting.gemCut(this, gems) }
    }
}
Spin.values().forEach { spin ->
    on_item_on_obj(obj = spinningObj, item = spin.unSpun) {
        player.queue { Spinning.Spin(this, spin) }
    }
}
