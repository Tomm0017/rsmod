package gg.rsmod.plugins.content.skills.runecrafting

import gg.rsmod.game.fs.def.ItemDef

Tiara.values.forEach { tiara ->

    val altar = tiara.altar
    val talisman = altar.talisman
    val altarObj = altar.altar

    talisman?.let {
        item_on_obj(obj = altarObj, item = Items.TIARA) { createTiara(player, it, tiara) }
        item_on_obj(obj = altarObj, item = talisman) { createTiara(player, it, tiara) }
    }
}

/**
 * Handles the creation of tiaras at a Runecrafting altar
 *
 * @param player    The player instance
 * @param talisman  The talisman required
 * @param def       The tiara definition
 */
fun createTiara(player: Player, talisman: Int, def: Tiara) {

    val tiaraDef = player.world.definitions.get(ItemDef::class.java, Items.TIARA)
    val talismanDef = player.world.definitions.get(ItemDef::class.java, talisman)
    val inventory = player.inventory

    if (!inventory.contains(tiaraDef.id)) {
        player.message("You need a ${tiaraDef.name} to bind to your talisman.")
        return
    }

    if (!inventory.contains(talismanDef.id)) {
        player.message("You need a ${talismanDef.name} to bind a ${tiaraDef.name} here.")
        return
    }

    if (inventory.remove(tiaraDef.id).hasSucceeded() && inventory.remove(talismanDef.id).hasSucceeded()) {
        val transaction = inventory.add(def.id)

        if (transaction.hasSucceeded()) {
            player.addXp(Skills.RUNECRAFTING, def.xp)
        }
    }
}