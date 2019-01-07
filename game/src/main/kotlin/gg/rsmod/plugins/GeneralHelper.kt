package gg.rsmod.plugins

import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.item.Item

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun String.plural(amount: Int): String {
    if (endsWith('s')) {
        return this
    }
    return if (amount != 1) this + "s" else this
}

fun ItemContainer.swap(to: ItemContainer, item: Item, beginSlot: Int, note: Boolean): Int {
    check(item.amount > 0)

    val copy = Item(item)

    val removal = remove(item.id, item.amount, assureFullRemoval = true, beginSlot = beginSlot)
    if (removal.hasFailed()) {
        return 0
    }
    val noted = if (note) copy.toNoted(definitions) else copy.toUnnoted(definitions)
    val addition = to.add(noted.id, noted.amount, assureFullInsertion = false)
    if (addition.hasSucceeded()) {
        /**
         * If there items were successfully added to [to], we copy the attributes
         * from the copy of [item].
         */
        val first = addition.items.firstOrNull { it.amount == 1 }
        first?.copyAttr(copy)
    } else {
        /**
         * If the items could not be added, we refund what's left over.
         */
        val refund = add(copy.id, addition.getLeftOver(), assureFullInsertion = true, beginSlot = beginSlot)
        refund.items.firstOrNull()?.copyAttr(copy)
        return 0
    }
    return addition.completed
}

/**
 * Similar to other [swap] method, however this does not copy any attributes.
 */
fun ItemContainer.swap(to: ItemContainer, item: Int, amount: Int, beginSlot: Int, note: Boolean): Int {
    val copy = Item(item, amount)

    val removal = remove(item, amount, assureFullRemoval = true, beginSlot = beginSlot)
    if (removal.hasFailed()) {
        return 0
    }
    val noted = if (note) copy.toNoted(definitions) else copy.toUnnoted(definitions)
    val addition = to.add(noted.id, noted.amount, assureFullInsertion = false)
    if (addition.hasFailed()) {
        /**
         * If the items could not be added, we refund what's left over.
         */
        val refund = add(copy.id, addition.getLeftOver(), assureFullInsertion = true, beginSlot = beginSlot)
        refund.items.firstOrNull()?.copyAttr(copy)
        return 0
    }
    return addition.completed
}