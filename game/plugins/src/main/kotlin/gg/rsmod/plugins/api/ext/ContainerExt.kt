package gg.rsmod.plugins.api.ext

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.World
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.container.ItemTransaction
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.service.marketvalue.ItemMarketValueService

fun ItemContainer.getNetworth(world: World): Long {
    val service = world.getService(ItemMarketValueService::class.java)
    var networth = 0L
    rawItems.forEach { item ->
        if (item != null) {
            val cost = service?.get(item.id) ?: world.definitions.getNullable(ItemDef::class.java, item.id)?.cost ?: 0
            networth += cost * item.amount
        }
    }
    return networth
}

/**
 * Transfer [item] from [this] container to [to] container.
 *
 * @return
 * The removal [ItemTransaction].
 */
fun ItemContainer.transfer(to: ItemContainer, item: Item, fromSlot: Int = -1, toSlot: Int = -1, note: Boolean = false, unnote: Boolean = false): ItemTransaction? {
    check(item.amount > 0)

    /*
     * Get the maximum amount of the item that can be transferred.
     */
    val amount = Math.min(item.amount, getItemCount(item.id))

    /*
     * Copy the item with the corrected amount.
     */
    val copy = Item(item, amount)

    /*
     * If we're transferring the whole item, make sure to copy its attributes.
     */
    if (amount >= item.amount) {
        copy.copyAttr(item)
    }

    /*
     * Turn the initial item into its noted or unnoted form, depending on [note]
     * and [unnote].
     */
    val finalItem = if (note) copy.toNoted(definitions) else if (unnote) copy.toUnnoted(definitions) else copy

    val add = to.add(finalItem.id, finalItem.amount, assureFullInsertion = false, beginSlot = toSlot)
    if (add.completed == 0) {
        return null
    }

    val remove = remove(item.id, add.completed, assureFullRemoval = true, beginSlot = fromSlot)
    if (remove.completed == 0) {
        add.revert(to)
        return null
    }

    /*
     * The first item added to [to] should copy any attributes that were on
     * the initial copy of [item].
     */
    add.first().item.copyAttr(copy)

    return remove
}

fun ItemTransaction.revert(from: ItemContainer) {
    items.forEach {
        from.remove(item = it.item, beginSlot = it.slot)
    }
}
