package gg.rsmod.game.model.container

import gg.rsmod.game.model.item.SlotItem

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
data class ItemTransaction(val requested: Int, val completed: Int, val items: List<SlotItem>) : Iterable<SlotItem> {

    override fun iterator(): Iterator<SlotItem> = items.iterator()

    /**
     * @return
     * The amount of items that did not go through with the transaction.
     *
     * For example
     * If the item is three (3) abyssal whips, but the container only has
     * two (2) slots available and does not have a stack type of
     * [ContainerStackType.STACK], this method will then return one (1)
     * as one abyssal whip was unable to be added.
     */
    fun getLeftOver(): Int = requested - completed

    /**
     * @return
     * true if the transaction was completely successful
     * ([completed] == [requested]).
     */
    fun hasSucceeded(): Boolean = completed == requested

    /**
     * @return
     * true if the transaction was not completely successful.
     * ([completed] != [requested]).
     */
    fun hasFailed(): Boolean = !hasSucceeded()
}