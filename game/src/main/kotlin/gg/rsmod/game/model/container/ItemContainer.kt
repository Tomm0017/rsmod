package gg.rsmod.game.model.container

import dev.openrune.cache.CacheManager.item
import gg.rsmod.game.model.container.key.ContainerKey
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.item.SlotItem

import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * An [ItemContainer] represents a collection of ordered [Item]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ItemContainer(val key: ContainerKey) : Iterable<Item?> {

    constructor(capacity: Int, stackType: ContainerStackType) : this(ContainerKey("", capacity, stackType))

    constructor(other: ItemContainer) : this(other.capacity, other.stackType) {
        for (i in 0 until capacity) {
            val item = if (other[i] != null) Item(other[i]!!) else null
            set(i, item)
        }
    }

    /**
     * The total amount of items that the container can hold at a time.
     */
    val capacity = key.capacity

    private val stackType = key.stackType

    private val items = Array<Item?>(capacity) { null }

    /**
     * A flag which indicates that the [items] has been modified since the last
     * game cycle.
     */
    var dirty = true

    override fun iterator(): Iterator<Item?> = items.iterator()

    /**
     * Gets the collection of nullable [Item]s in this container.
     */
    val rawItems: Array<Item?> = items

    /**
     * Checks if the container has an [Item] which has the same [Item.id] as
     * [item].
     */
    fun contains(item: Int): Boolean = items.any { it?.id == item }

    /**
     * Checks if the container has an [Item] which has the same [Item.id] as
     * [item] or any of the values (if any) in [others].
     */
    fun containsAny(item: Int, vararg others: Int): Boolean = items.any { it != null && (it.id == item || it.id in others) }

    /**
     * Checks if the container has an [Item] which has the same [Item.id] as
     * [itemId] in the specific [slot].
     */
    fun hasAt(slot: Int, itemId: Int): Boolean = items[slot]?.id == itemId

    /**
     * Gets the most-left/first index(slot) that is not occupied by an [Item].
     * Defaults to -1 if none is found.
     */
    val nextFreeSlot: Int get() = items.indexOfFirst { it == null }

    /**
     * Calculates the amount of available slots in this container.
     */
    val freeSlotCount: Int get() = items.count { it == null }

    /**
     * Gets the most-right/last index(slot) that is not occupied by an [Item] but it next to an [Item].
     * Defaults to -1 if none is found.
     */
    fun getLastFreeSlot(): Int {
        var lastEmpty = -1;
        for (index in items.indices) {
            if (items[index] == null)
                lastEmpty = index
            else
                break
        }
        return lastEmpty
    }


    /**
     * @TODO Refactor.
     */
    fun getLastFreeSlot(startIndex: Int): Int {
        var lastEmpty = -1
        items.indices.reversed().forEach {
            if (it > startIndex) {
                if (items[it] == null) {
                    lastEmpty = it
                }
            }
        }
        return lastEmpty
    }
    fun getLastFreeSlotReversed(): Int {
        var lastEmpty = -1;
        for (index in items.indices.reversed()) {
            if (items[index] == null)
                lastEmpty = index
            else
                break
        }
        return lastEmpty
    }

    /**
     * Calculates the amount of slots that are occupied in this container.
     */
    val occupiedSlotCount: Int get() = items.count { it != null }

    /**
     * Check if the container is full.
     */
    val isFull: Boolean get() = items.all { it != null }

    /**
     * Check if the container is completely empty.
     */
    val isEmpty: Boolean get() = items.none { it != null }

    /**
     * @return
     * true if the container has any item at all which is not null.
     */
    val hasAny: Boolean get() = items.any { it != null }

    /**
     * @return
     * true if the container has any free slot available.
     */
    val hasSpace: Boolean get() = nextFreeSlot != -1

    /**
     * Calculate the total amount of items in this container who's [Item.id]
     * matches [itemId].
     */
    fun getItemCount(itemId: Int): Int {
        var amount: Long = 0

        /*
         * We could stop the loop once the first matching item is found, if the
         * container has a [stackType] of [ContainerStackType.STACK], or if the
         * item's metadata describes it as a stackable item. However, we won't
         * do this since there are items that aren't stackable even when the [stackType]
         * equals [ContainerStackType.STACK], such as degradeable or chargeable items.
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
     * @param skipAttrItems
     * This flag indicates if [gg.rsmod.game.model.item.Item]s which have
     * [gg.rsmod.game.model.item.ItemAttribute]s should not be taken into
     * account (should be skipped) when iterating through the [items] to
     * find one with the same [itemId] to return its index in the container.
     *
     * @return
     * [-1] if no item with [itemId] could be found.
     */
    fun getItemIndex(itemId: Int, skipAttrItems: Boolean): Int {
        for (i in 0 until capacity) {
            if (items[i]?.id == itemId && (!skipAttrItems || !items[i]!!.hasAnyAttr())) {
                return i
            }
        }
        return -1
    }

    /**
     * Creates a map that holds the [Item]s in this container, with the slot of
     * the item being the key and the item being the value.
     */
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
     * Remove any and all items from the container.
     */
    fun removeAll() {
        for (i in 0 until capacity) {
            set(i, null)
        }
    }

    /**
     * Adds an item with id of [item] and quantity of [amount] to this container.
     *
     * @param [item]
     * The item id.
     *
     * @param [amount]
     * The quantity of the item.
     *
     * @param assureFullInsertion
     * If true, we make sure the container can hold [amount] of [item], taking
     * into account the item's metadata and the container's [ContainerStackType].
     * If false, it will try to fill the container with as many [item]s as it
     * can fit before the container is full.
     *
     * @param forceNoStack
     * true if the item's metadata and container's [stackType] should be ignored,
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
     *
     * @return
     * An [ItemTransaction] that contains relevant information on how successful
     * the operation was. The transaction implements [Iterable]. Its iterable
     * elements are made up of any items that were successfully added to the
     * container. This can be used to perform an operation, such as attaching
     * attributes, to the item(s) that were added.
     *
     * @see ItemTransaction
     */
    fun add(item: Int, amount: Int = 1, assureFullInsertion: Boolean = true, forceNoStack: Boolean = false, beginSlot: Int = -1): ItemTransaction {
        val def = item(item)

        /*
         * Should the item stack?
         */
        val stack = !forceNoStack && stackType != ContainerStackType.NO_STACK && (def.stackable || stackType == ContainerStackType.STACK)

        /*
         * We don't need to calculate the previous amount unless the item is going
         * to stack.
         */
        val previousAmount = if (stack) getItemCount(item) else 0

        if (previousAmount == Int.MAX_VALUE) {
            return ItemTransaction(amount, 0, emptyList())
        }

        /*
         * The amount of free slots in the container.
         */
        val freeSlotCount = freeSlotCount

        /*
         * If the player has no more free slots and either `stack`
         * is not set or the container does not have [item] at all,
         * the transaction will fail.
         */
        if (freeSlotCount == 0 && (!stack || stack && previousAmount == 0)) {
            return ItemTransaction(amount, 0, emptyList())
        }

        if (assureFullInsertion) {
            /*
             * If the item will stack, but the previous item stack can't hold
             * [amount] more of the item, the transaction will fail.
             */
            if (stack && previousAmount > Int.MAX_VALUE - amount) {
                return ItemTransaction(amount, 0, emptyList())
            }

            /*
             * If the item will not stack and there's less free slots than [amount],
             * the transaction will fail.
             */
            if (!stack && amount > freeSlotCount) {
                return ItemTransaction(amount, 0, emptyList())
            }
        } else {
            /*
             * Even if our caller has stated to not [assureFullInsertion], there's
             * still no reason to have the other logic execute if we know that
             * not even a single [item] item can be added.
             */
            if (stack && previousAmount == Int.MAX_VALUE) {
                return ItemTransaction(amount, 0, emptyList())
            } else if (!stack && freeSlotCount == 0) {
                return ItemTransaction(amount, 0, emptyList())
            }
        }

        var completed = 0
        val added = mutableListOf<SlotItem>()

        if (!stack) {
            /*
             * Placeholders only occur in stackable containers, like bank.
             * No need to do anything with placeholder here.
             */
            val startSlot = Math.max(0, beginSlot)
            for (i in startSlot until capacity) {
                if (items[i] != null) {
                    continue
                }
                val add = Item(item)
                set(i, add)
                added.add(SlotItem(i, add))
                if (++completed >= amount) {
                    break
                }
            }
        } else {
            var stackIndex = getItemIndex(itemId = item, skipAttrItems = true)
            if (stackIndex == -1) {
                if (beginSlot == -1) {
                    stackIndex = nextFreeSlot
                } else {
                    for (i in beginSlot until capacity) {
                        if (items[i] == null) {
                            stackIndex = i
                            break
                        }
                    }
                }
                if (stackIndex == -1) {
                    /*
                     * This shouldn't happen as we have already made sure that
                     * at least one item, whether stackable or not, can fit in
                     * our container.
                     */
                    logger.error(RuntimeException("Unable to find a free slot for a stackable item. [capacity=$capacity, item=$item, quantity=$amount]")) {}
                    return ItemTransaction(amount, completed, emptyList())
                }
            }

            val stackAmount = get(stackIndex)?.amount ?: 0
            val total = Math.min(Int.MAX_VALUE.toLong(), (stackAmount).toLong() + amount.toLong()).toInt()

            val add = Item(item, total)
            set(stackIndex, add)
            added.add(SlotItem(stackIndex, add))
            completed = total - stackAmount
        }
        return ItemTransaction(amount, completed, added)
    }

    /**
     * Adds an [Item] to our container.
     *
     * Keep in mind, that just because you're using an [Item] object, does not
     * mean that the same [Item] instance will be added to the container. The
     * [item] is simply used as a reference for the item id and amount that should
     * be added. Nothing else in it, including its attributes, are taken into
     * account.
     */
    fun add(item: Item, assureFullInsertion: Boolean = true, forceNoStack: Boolean = false, beginSlot: Int = -1): ItemTransaction {
        return add(item = item.id, amount = item.amount, assureFullInsertion = assureFullInsertion,
                forceNoStack = forceNoStack, beginSlot = beginSlot)
    }

    /**
     * Remove [amount] of [item] in the the container.
     *
     * @param item
     * The item id.
     *
     * @param amount
     * The amount of [item]s to try and remove. There is no guarantee that this
     * method will be able to remove all the [amount] of items. This is true
     * when the container doesn't have as many [item]s as [amount].
     *
     * To get the amount of items that were removed, see [ItemTransaction.completed].
     * To get the amount of items that couldn't be removed, see [ItemTransaction.getLeftOver].
     *
     * @param assureFullRemoval
     * If true, we make sure the container has [amount] or more items who's
     * [Item.id] matches [item], before attempting to remove any.
     * If false, it will remove any item with [Item.id] of [item] which it can find
     * until [amount] have been removed or until the container has no more.
     *
     * @param beginSlot
     * The search for [item] in the [items] array will begin from this index and
     * will sequentially increment until either 1) [amount] of [item]s have been
     * removed or 2) we have iterated through every single [Item] in [items].
     *
     * @return
     * An [ItemTransaction] that contains relevant information on how successful
     * the operation was. The transaction implements [Iterable]. Its iterable
     * elements are made up of any items that were successfully and fully removed
     * from the container. The requirement for an item to be added as an iterable
     * element is for it to have been completely removed from its stack in the
     * container. For example if you remove 10 coins, but 2 coins have been left
     * on the same stack, the item will NOT be added to the iterable elements.
     *
     * @see ItemTransaction
     */
    fun remove(item: Int, amount: Int = 1, assureFullRemoval: Boolean = false, beginSlot: Int = -1): ItemTransaction {
        val hasAmount = getItemCount(item)

        if (assureFullRemoval && hasAmount < amount) {
            return ItemTransaction(amount, 0, emptyList())
        } else if (!assureFullRemoval && hasAmount < 1) {
            return ItemTransaction(amount, 0, emptyList())
        }

        var totalRemoved = 0
        val removed = mutableListOf<SlotItem>()

        val skippedIndices = if (beginSlot != -1) 0 until beginSlot else null

        val index = if (beginSlot != -1) beginSlot else 0
        for (i in index until capacity) {
            val curItem = items[i] ?: continue
            if (curItem.id == item) {
                val removeCount = Math.min(curItem.amount, amount - totalRemoved)
                totalRemoved += removeCount

                curItem.amount -= removeCount
                if (curItem.amount == 0) {
                    val removedItem = Item(items[i]!!)
                    items[i] = null
                    removed.add(SlotItem(i, removedItem))
                }

                if (totalRemoved == amount) {
                    break
                }
            }
        }

        /*
         * If we specified a [beginSlot] to begin the search, but we were not able
         * to remove [amount] of [item] items, then we go over the skipped indices.
         * This is done to ensure that [assureFullRemoval] will always provide
         * accurate results.
         *
         * Example: One abyssal whip in slot 0 and another in slot 10, we call [remove]
         * with [beginSlot] of 5 and [assureFullRemoval] as true. Our initial
         * check to make sure the container has enough of the item will succeed,
         * however the loop would only iterate through items in index 5-[capacity].
         * This would only remove one of the two items if we would not go over the
         * skipped indices.
         */
        if (skippedIndices != null && totalRemoved < amount) {
            for (i in skippedIndices) {
                val curItem = items[i] ?: continue
                if (curItem.id == item) {
                    val removeCount = Math.min(curItem.amount, amount - totalRemoved)
                    totalRemoved += removeCount

                    curItem.amount -= removeCount
                    if (curItem.amount == 0) {
                        val removedItem = Item(items[i]!!)
                        items[i] = null
                        removed.add(SlotItem(i, removedItem))
                    }

                    if (totalRemoved == amount) {
                        break
                    }
                }
            }
        }

        if (totalRemoved > 0) {
            dirty = true
        }
        return ItemTransaction(amount, totalRemoved, removed)
    }

    /**
     * Alias for
     * ```
     * remove(id: Int, amount: Int, assureFullRemoval: Boolean = false, beginSlot: Int = -1)
     * ```
     */
    fun remove(item: Item, assureFullRemoval: Boolean = false, beginSlot: Int = -1): ItemTransaction = remove(item.id, item.amount, assureFullRemoval, beginSlot)

    /**
     * replaces [Item] in container with optional slot awareness
     * and is generally for generating an [Item] from another
     *   Note| most useful for operations which produce an [Item]
     *   of another form (e.g. and empty vial becoming a vial of water)
     *
     * @param remove - [Item.id] of [Item] to replace
     * @param add - [Item.id] of [Item] to add
     * @param slot - the destination slot to remove and add to
     *   Note| slot default of -1 is not slot-aware and works on first available
     *
     * @return whether or not the replace operation completed as expected -> true|false
     */
    fun replace(remove: Int, add: Int, slot: Int = -1): Boolean {
        return if(remove(remove, beginSlot = slot).hasSucceeded())
            add(add, beginSlot = slot).hasSucceeded()
        else
            false
    }

    /**
     * wrapper for [replace] method with added [Item] requirement
     * only if the specified [Item] is also in container
     *   Note| really only useful for inventory replacements ALTHOUGH it could be adapted
     *   to mock a container with the [required] [Item] added upon desired conditions
     *
     * @param required - [Item.id] of [Item] required in container for replace
     */
    fun replaceWithItemRequirement(remove: Int, add: Int, required: Int, slot: Int = -1): Boolean {
        return if(contains(required))
            replace(remove, add, slot)
        else
            false
    }

    /**
     * wrapper for [replace] which works to [remove] an [Item] as a requirement
     * in a hopefully conscious way to preserve proper removal along with replacement
     *   Note| most useful for operations which consume a secondary [Item] during
     *   production of another form (e.g. clean herb is lost when a vial of water
     *   becomes an unfinished potion)
     *
     * @param other - [Item] to remove along with [replace] operation
     *   Note| the use of [Item] and not [Item.id] is purposed to permit amounts
     *   and imposes assurance checks in container operations
     */
    fun replaceAndRemoveAnother(remove: Int, add: Int, other: Item, slot: Int = -1, otherSlot: Int = -1): Boolean {
        val taken = remove(other, assureFullRemoval = true, beginSlot = otherSlot).hasSucceeded()
        return if(replace(remove, add, slot) && taken)
            true
        else if(taken){
            add(other, assureFullInsertion = true, beginSlot = otherSlot)
            false
        } else
            false
    }

    /**
     * wrapper for [replaceAndRemoveAnother] which requires the container to contain [required]
     */
    fun replaceAndRemoveAnotherWithItemRequirement(remove: Int, add: Int, other: Item, required: Int, slot: Int = -1, otherSlot: Int = -1): Boolean {
        return if(contains(required))
            replaceAndRemoveAnother(remove, add, other, slot, otherSlot)
        else
            false
    }

    /**
     * works to [replace] two [Item]s involved in [Item] productions where
     * both input [Item]s have a supplemental by-product [Item]
     *
     * @param removeItem - [Item.id] of "main" [Item] involved to be replaced
     * @param addItem - [Item.id] of "main" [Item] involved to be replaced with
     * @param slot - the intended destination slot in container for the "main" [Item] replacement
     *   Note| params prefixed with "other" follow same conventions as "main" [Item] params
     *   however the imposed meaning of "main" and "other" reside with method caller and this method
     *   couldn't really care less which is which or if either [Item] is using slot awareness but
     *   does allow for flexibility of either inherently as wrapped calls handle slot-aware operations
     *
     * @return whether or not the replace operation completed as expected -> true|false
     */
    fun replaceBoth(removeItem: Int, addItem: Int, otherItem: Int, otherAddItem:Int, slot: Int = -1, otherSlot: Int = -1): Boolean {
        val taken = replace(otherItem, otherAddItem, otherSlot)
        return if(replace(removeItem, addItem, slot) && taken)
            true
        else if(taken){
            replace(otherAddItem, otherItem, otherSlot)
            false
        } else
            false
    }

    /**
     * Swap slots of items in slot [from] and [to].
     */
    fun swap(from: Int, to: Int) {
        val copy = items[from]
        set(from, items[to])
        set(to, copy)
    }

    /**
     * Shifts the items in the [ItemContainer] to the appropriate position
     */
    fun shift() {
        val newItems = Array<Item?>(capacity) { null }

        var index = 0
        for (i in 0 until capacity) {
            val item = this[i] ?: continue
            newItems[index++] = item
        }

        removeAll()

        for (i in 0 until capacity) {
            set(i, newItems[i])
        }
    }

//    fun shift(from: Int): Int {
//        println("[$from] Before: ")
//        items.forEachIndexed { index, it ->
//            if (index < 20)
//            println("$index = $it")
//        }
//        val copyOfItems = items
//        var index = from
//        while (copyOfItems[index] == null) {
//            index++
//        }
//        println("After: ")
//        copyOfItems.copyOfRange(index, capacity).forEachIndexed { i, it ->
//            if (i < 20) {
//                println("$i = $it")
//            }
//            set(i, it)
//        }
//        return index - from
//    }
//
//    fun shiftV2(from: Int): Int {
//        var nextNonNullIndex = from
//        while (nextNonNullIndex < items.size && items[nextNonNullIndex] == null) {
//            nextNonNullIndex++
//        }
//
//        if (nextNonNullIndex >= items.size) {
//            // No non-null value found after the given index
//            return nextNonNullIndex
//        }
//
//        val shiftAmount = nextNonNullIndex - from
//        for (i in from until items.size - shiftAmount) {
//            items[i] = items[i + shiftAmount]
//        }
//
//        for (i in items.size - shiftAmount until items.size) {
//            items[i] = null
//        }
//        return nextNonNullIndex
//    }

    operator fun get(index: Int): Item? = items[index]

    operator fun set(index: Int, item: Item?) {
        items[index] = item
        dirty = true
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}
