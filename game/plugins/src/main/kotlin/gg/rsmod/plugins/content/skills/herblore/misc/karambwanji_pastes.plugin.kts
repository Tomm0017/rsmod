package gg.rsmod.plugins.content.skills.herblore.misc

import gg.rsmod.plugins.content.skills.herblore.pestle.Crushables.*

/**
 * Karambwanji paste products
 */
on_item_on_item(KARAMBWANJI_RAW.itemOut, Items.BURNT_JOGRE_BONES){
    player.inventory.replaceAndRemoveAnother(Items.BURNT_JOGRE_BONES, Items.PASTY_JOGRE_BONES, KARAMBWANJI_RAW.itemOut.toItem())
}
on_item_on_item(KARAMBWANJI_RAW.itemOut, Items.JOGRE_BONES){
    player.inventory.replaceAndRemoveAnother(Items.JOGRE_BONES, Items.PASTY_JOGRE_BONES_3131, KARAMBWANJI_RAW.itemOut.toItem())
}
on_item_on_item(KARAMBWANJI_COOKED.itemOut, Items.BURNT_JOGRE_BONES){
    player.inventory.replaceAndRemoveAnother(Items.BURNT_JOGRE_BONES, Items.PASTY_JOGRE_BONES_3129, KARAMBWANJI_COOKED.itemOut.toItem())
}
on_item_on_item(KARAMBWANJI_COOKED.itemOut, Items.JOGRE_BONES){
    player.inventory.replaceAndRemoveAnother(Items.JOGRE_BONES, Items.PASTY_JOGRE_BONES_3132, KARAMBWANJI_COOKED.itemOut.toItem())
}