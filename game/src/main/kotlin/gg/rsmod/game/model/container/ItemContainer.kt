package gg.rsmod.game.model.container

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.item.Item
import org.apache.logging.log4j.LogManager

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemContainer(val definitions: DefinitionSet, val capacity: Int, val stackType: ContainerStackType) : Iterable<Item?> {

    companion object {
        private val logger = LogManager.getLogger(ItemContainer::class.java)
    }

    private val items = Array<Item?>(capacity) { null }

    var dirty = true

    override fun iterator(): Iterator<Item?> = items.iterator()

    fun getBackingArray(): Array<Item?> = items

    fun getNextFreeSlot(): Int = items.indexOfFirst { it == null }

    fun getFreeSlotCount(): Int = items.count { it == null }

    fun getOccupiedSlotCount(): Int = items.count { it != null }

    fun isFull(): Boolean = items.all { it != null }

    fun isEmpty(): Boolean = items.none { it != null }

    fun getItemCount(itemId: Int): Int {
        var amount: Long = 0

        /**
         * We could stop the loop once the first one is found if the container
         * has a [stackType] of [ContainerStackType.STACK], or if the item
         * metadata describes it as a stackable item. However, we won't do this
         * since there are items that aren't stackable even on [ContainerStackType.STACK],
         * such as degradeable or chargeable items.
         *
         * The cost of iterating through tens or hundreds of items, when used
         * appropriately, shouldn't be expensive regardless.
         */
        for (item in items) {
            if (item?.id == itemId) {
                amount += item.amount
            }
        }

        return Math.min(Int.MAX_VALUE.toLong(), amount).toInt()
    }

    /**
     * Get the index of [itemId] in relation to [items].
     *
     * @return
     * [-1] if no item with [itemId] could be found.
     */
    private fun getItemIndex(itemId: Int): Int {
        for (i in 0 until capacity) {
            if (items[i]?.id == itemId) {
                return i
            }
        }
        return -1
    }

    fun setItems(items: Map<Int, Item>) {
        items.forEach { index, item ->
            set(index, item)
        }
    }

    fun toMap(): Map<Int, Item> {
        val map = hashMapOf<Int, Item>()
        items.forEachIndexed { index, item ->
            if (item != null) {
                map[index] = item
            }
        }
        return map
    }

    /**
     * Remove all items from the container.
     */
    fun removeAll() {
        for (i in 0 until capacity) {
            set(i, null)
        }
    }

    /**
     * Adds an item with id of [id] and quantity of [amount] to this container.
     *
     * @param [id]
     * The item id.
     *
     * @param [amount]
     * The quantity of the item.
     *
     * @param assureFullInsertion
     * If [true], we make sure the container can hold [amount] of [id], taking
     * into account the item's metadata and the container's [ContainerStackType].
     * If [false], it will try to fill the container with as many [id]s as it
     * can before the container is full.
     *
     * @param forceNoStack
     * [true] if the item's metadata and container's [stackType] should be ignored,
     * and the item should never stack at all. This is useful for items that should
     * not stack, such as degradeable or chargeable items.
     *
     * @param beginSlot
     * If you would like to place the item in a certain slot, set this to that
     * value. However, if for whatever reason that slot is already taken, we will
     * increment and keep trying to insert the item on the next available slot.
     *
     * Note: if the item is determined to be stackable, this will be taken as a
     * suggestion as any pre-existing stack's index of the item will be used as
     * the slot for this transaction.
     */
    fun add(id: Int, amount: Int, assureFullInsertion: Boolean = true, forceNoStack: Boolean = false, beginSlot: Int = -1): ItemTransaction {
        val def = definitions.get(ItemDef::class.java, id)
        val stack = !forceNoStack && (def.isStackable() || stackType == ContainerStackType.STACK)

        /**
         * We don't need to calculate the previous amount unless the item is going
         * to stack.
         */
        val previousAmount = if (stack) getItemCount(id) else 0

        if (previousAmount == Int.MAX_VALUE) {
            return ItemTransaction(amount, 0)
        }

        val freeSlotCount = getFreeSlotCount()

        if (assureFullInsertion) {
            /**
             * If the item will stack, but the previous item stack can't hold
             * [amount] more of the item, the transaction will fail.
             */
            if (stack && previousAmount > Int.MAX_VALUE - amount) {
                return ItemTransaction(amount, 0)
            }

            /**
             * If the item will not stack and there's less free slots than [amount],
             * the transaction will fail.
             */
            if (!stack && amount > freeSlotCount) {
                return ItemTransaction(amount, 0)
            }
        } else {
            /**
             * Even if our caller has stated to not [assureFullInsertion], there's
             * still no reason to have the other logic execute if we know that
             * not even a single [id] item can be added.
             */
            if (stack && previousAmount == Int.MAX_VALUE) {
                return ItemTransaction(amount, 0)
            } else if (!stack && freeSlotCount == 0) {
                return ItemTransaction(amount, 0)
            }
        }

        var completed = 0

        if (!stack) {
            val startSlot = Math.max(0, beginSlot)
            for (i in startSlot until capacity) {
                if (items[i] != null) {
                    continue
                }
                set(i, Item(id))
                if (++completed >= amount) {
                    break
                }
            }
        } else {
            var stackIndex = getItemIndex(id)
            if (stackIndex == -1) {
                if (beginSlot == -1) {
                    stackIndex = getNextFreeSlot()
                } else {
                    for (i in beginSlot until capacity) {
                        if (items[i] == null) {
                            stackIndex = i
                            break
                        }
                    }
                }
                if (stackIndex == -1) {
                    /**
                     * This shouldn't happen as we have already made sure that
                     * at least one item, whether stackable or not, can fit in
                     * our container.
                     */
                    logger.error("Unable to find a free slot for a stackable item. [capacity=$capacity, item=$id, quantity=$amount]") // print capacity as only form of distinction
                    return ItemTransaction(amount, completed)
                }
            }

            val previous = get(stackIndex)
            val total = Math.min(Int.MAX_VALUE.toLong(), (previous?.amount ?: 0).toLong() + amount.toLong()).toInt()

            set(stackIndex, Item(id, total))
            completed = total - (previous?.amount ?: 0)
        }
        return ItemTransaction(amount, completed)
    }

    /**
     * Adds an [Item] to our container. We also use the item to see if the
     * transaction should use the "no-stack" flag, which means that even if
     * the container has a type of [ContainerStackType.STACK] or if the item
     * metadata specifies it's stackable, it will will not stack with any other
     * item with the same id on the container.
     *
     * Any [Item] that has any [gg.rsmod.game.model.item.ItemAttribute] will not
     * stack with other items of the same id.
     */
    fun add(item: Item, assureFullInsertion: Boolean = true, beginSlot: Int = -1): ItemTransaction {
        return add(id = item.id, amount = item.amount, assureFullInsertion = assureFullInsertion,
                forceNoStack = item.hasAnyAttr(), beginSlot = beginSlot)
    }

    /**
     * Remove [amount] of [id] in the the container.
     *
     * @param id
     * The item id.
     *
     * @param amount
     * The amount of [id]s to try and remove. There is no guarantee that this
     * method will be able to remove all the [amount] of items. This is true
     * when the container doesn't have as many [id]s as [amount].
     *
     * To get the amount of items that were removed, see [ItemTransaction.completed].
     * To get the amount of items that couldn't be removed, see [ItemTransaction.getLeftOver].
     *
     * @param assureFullRemoval
     * If [true], we make sure the container has [amount] or more [id]s before
     * attempting to remove any.
     * If [false], it will remove any [id] it can find until [amount] has been
     * removed.
     *
     * @param fromIndex
     * The search for [id] in the [items] array will begin from this index and
     * will sequentially increment until either 1) [amount] of [id]s has been
     * removed or 2) the index has reached [capacity].
     */
    fun remove(id: Int, amount: Int = 1, assureFullRemoval: Boolean = true, fromIndex: Int = -1): ItemTransaction {
        val hasAmount = getItemCount(id)

        if (assureFullRemoval && hasAmount < amount) {
            return ItemTransaction(amount, 0)
        } else if (!assureFullRemoval && hasAmount < 1) {
            return ItemTransaction(amount, 0)
        }

        var removed = 0

        val skippedIndices = if (fromIndex != -1) 0 until fromIndex else null

        val index = if (fromIndex != -1) fromIndex else 0
        for (i in index until capacity) {
            val item = items[i] ?: continue
            if (item.id == id) {
                val removeCount = Math.min(item.amount, amount - removed)
                removed += removeCount

                item.amount -= removeCount
                if (item.amount == 0) {
                    items[i] = null
                }

                if (removed == amount) {
                    break
                }
            }
        }

        /**
         * If we specified a [fromIndex] to begin the search, but we were not able
         * to remove [amount] of [id] items, then we go over the skipped indices.
         * This is done to ensure that [assureFullRemoval] will always provide
         * accurate results.
         *
         * Example: 1 abyssal whip in slot 0 and 1 in slot 10, we call [remove]
         * with [fromIndex] of [5] and [assureFullRemoval] as [true]. Our initial
         * check to make sure the container has enough of the item will succeed,
         * however the loop would only iterate through items in index 5-[capacity].
         */
        if (skippedIndices != null && removed < amount) {
            for (i in skippedIndices) {
                val item = items[i] ?: continue
                if (item.id == id) {
                    val removeCount = Math.min(item.amount, amount - removed)
                    removed += removeCount

                    item.amount -= removeCount
                    if (item.amount == 0) {
                        items[i] = null
                    }

                    if (removed == amount) {
                        break
                    }
                }
            }
        }

        if (removed > 0) {
            dirty = true
        }
        return ItemTransaction(amount, removed)
    }

    operator fun get(index: Int): Item? = items[index]

    private fun set(index: Int, item: Item?) {
        items[index] = item
        dirty = true
    }
}