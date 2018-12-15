package gg.rsmod.game.model.container

import gg.rsmod.game.model.item.Item

/**
 * Represents a transaction when adding or removing an item from [ItemContainer].
 *
 * @param requested
 * The amount of the item that was requested on this transaction; which can include,
 * but is not limited to, the amount that should be added or removed from the
 * container.
 *
 * @param completed
 * How much of the [requested] amount was completed. This value can range from
 * [0] to [requested].
 *
 * @param items
 * A [List] of items that were successful on their transaction to the container.
 *
 * @see [ItemContainer.add]
 * @see [ItemContainer.remove]
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class ItemTransaction(val requested: Int, val completed: Int, val items: List<Item>) : Iterable<Item> {

    override fun iterator(): Iterator<Item> = items.iterator()

    fun getLeftOver(): Int = requested - completed

    fun hasSucceeded(): Boolean = completed == requested

    fun hasFailed(): Boolean = !hasSucceeded()
}