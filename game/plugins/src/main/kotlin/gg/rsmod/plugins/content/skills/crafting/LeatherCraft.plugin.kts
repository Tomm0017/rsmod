package gg.rsmod.plugins.content.skills.crafting

import gg.rsmod.plugins.content.skills.crafting.data.Leathers
import gg.rsmod.plugins.content.skills.crafting.action.LeatherAction
import gg.rsmod.game.model.attr.OTHER_ITEM_ID_ATTR
import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID

val leatherDefs = Leathers.leatherDefinitions
val leatherAction = LeatherAction(world.definitions)

leatherDefs.keys.forEach { leathers ->
    on_item_on_item(item1 = Items.NEEDLE, item2 = leathers) { craftLeather(player, leathers) }
}
fun craftLeather(player: Player, leatherItem: Int) {
    val craftables = leatherDefs[leatherItem]?.values?.map { craftable -> craftable.id }?.toIntArray() ?: return
    player.queue { produceItemBox(*craftables, type = 12, logic = ::leather) }
}

fun leather(player: Player, item: Int, amount: Int) {
    val leatherItem = if(leatherDefs.containsKey(player.attr[INTERACTING_ITEM_ID])){
        player.attr[INTERACTING_ITEM_ID]
    } else if(leatherDefs.containsKey(player.attr[OTHER_ITEM_ID_ATTR])){
        player.attr[OTHER_ITEM_ID_ATTR]
    } else {
        null
    }
    leatherItem ?: return

    val leatherOptions = leatherDefs[leatherItem]?.get(item) ?: return

    player.interruptQueues()
    player.resetInteractions()
    player.queue { leatherAction.leathers(this, leatherItem, leatherOptions, amount) }
}