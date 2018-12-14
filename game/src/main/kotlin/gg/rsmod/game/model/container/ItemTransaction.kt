package gg.rsmod.game.model.container

/**
 * Represents a valid transaction when adding or removing an item from [ItemContainer].
 *
 * @see [ItemContainer.add]
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class ItemTransaction(val requested: Int, val completed: Int) {

    fun getLeftOver(): Int = requested - completed

    fun hasSucceeded(): Boolean = completed == requested

    fun hasFailed(): Boolean = !hasSucceeded()
}