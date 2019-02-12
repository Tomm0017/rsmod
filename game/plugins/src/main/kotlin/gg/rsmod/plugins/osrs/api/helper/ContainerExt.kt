package gg.rsmod.plugins.osrs.api.helper

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.World
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.osrs.service.item.ItemValueService

/**
 * @author Tom <rspsmods@gmail.com>
 */
fun ItemContainer.networth(world: World): Long {
    val service = world.getService(ItemValueService::class.java).orElse(null)
    var networth = 0L
    getBackingArray().forEach { item ->
        if (item != null) {
            val cost = service?.get(item.id) ?: world.definitions.getNullable(ItemDef::class.java, item.id)?.cost ?: 0
            networth += cost * item.amount
        }
    }
    return networth
}

fun ItemContainer.swap(to: ItemContainer, item: Item, beginSlot: Int, note: Boolean): Int {
    check(item.amount > 0)

    val copy = Item(item)

    /**
     * Try to remove the items from this container.
     */
    val removal = remove(item.id, item.amount, assureFullRemoval = true, beginSlot = beginSlot)
    if (removal.hasFailed()) {
        return 0
    }

    /**
     * Turn the initial item into its noted or unnoted form, depending on [note].
     */
    val noted = if (note) copy.toNoted(definitions) else copy.toUnnoted(definitions)

    /**
     * Try to add as many of the requested amount of the item to the container [to].
     * If any of the item could not be added to the container [to], we refund it
     * to this container.
     */
    val addition = to.add(noted.id, noted.amount, assureFullInsertion = false)
    if (addition.hasSucceeded()) {
        /**
         * If there items were successfully added to [to], we copy the attributes
         * from the initial [item].
         */
        val first = addition.items.firstOrNull { it.item.amount == 1 }
        first?.item?.copyAttr(copy)
    } else {
        val refund = add(copy.id, addition.getLeftOver(), assureFullInsertion = true, beginSlot = beginSlot)
        /**
         * As the logic could've only gotten this far if the initial item was
         * completely removed from [ItemContainer.this] container, the initial
         * item is now gone. We want the refunded item to copy the attributes
         * of the original.
         *
         * This is so that if, for example, you try to transfer 2 toxic blowpipes,
         * both were removed, but only 1 was transferred  the other blowpipe will
         * get its initial attributes refunded.
         */
        refund.items.firstOrNull()?.item?.copyAttr(copy)
        return 0
    }
    return addition.completed
}

/**
 * Similar to other [swap] method, however this does not copy any attributes.
 */
fun ItemContainer.swap(to: ItemContainer, item: Int, amount: Int, beginSlot: Int, note: Boolean): Int {
    val copy = Item(item, amount)

    /**
     * Try to remove the items from this container.
     */
    val removal = remove(item, amount, assureFullRemoval = true, beginSlot = beginSlot)
    if (removal.hasFailed()) {
        return 0
    }

    /**
     * Turn the initial item into its noted or unnoted form, depending on [note].
     */
    val noted = if (note) copy.toNoted(definitions) else copy.toUnnoted(definitions)

    /**
     * Try to add as many of the requested amount of the item to the container [to].
     * If any of the item could not be added to the container [to], we refund it
     * to this container.
     */
    val addition = to.add(noted.id, noted.amount, assureFullInsertion = false)

    if (addition.getLeftOver() > 0) {
        val refund = add(copy.id, addition.getLeftOver(), assureFullInsertion = true, beginSlot = beginSlot)

        /**
         * As the logic could've only gotten this far if the initial item was
         * completely removed from [ItemContainer.this] container, the initial
         * item is now gone. We want the refunded item to copy the attributes
         * of the original.
         *
         * This is so that if, for example, you try to transfer 2 toxic blowpipes,
         * both were removed, but only 1 was transferred  the other blowpipe will
         * get its initial attributes refunded.
         */
        refund.items.firstOrNull()?.item?.copyAttr(copy)
    }
    return addition.completed
}
