package gg.rsmod.game.model.item

/**
 * An [ItemAttribute] is a broad-use attribute key which can be used for different
 * effects on different items. [Item]s that have attributes will stop being tradeable
 * and can no longer stack with other items of the same id.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class ItemAttribute {

    /**
     * Can represent any type of charge on an item.
     */
    CHARGES,

    /**
     * Represents the degrade percentage of an item. This can range from
     * 0 to 10000. 0 represents 0.0% and 10000 represents 100.00%. This
     * value is an integer, but is presented as a double (percentage) to the
     * player.
     */
    DEGRADE,

    /**
     * Some items can have another item 'attached' to them in some form or
     * another.
     *
     * Example: Toxic blowpipe can have darts attached to it.
     * Example: Rune pouches can have rune essence or pure essence attached to it.
     */
    ATTACHED_ITEM_ID,

    /**
     * The amount of [ATTACHED_ITEM_ID]s left on the item.
     */
    ATTACHED_ITEM_COUNT,

    /**
     * The amount of attacks that this item has dealt to a target. This attribute
     * can be reset at any point, such as for the Toxic blowpipe resetting every
     * three attacks.
     */
    ATTACK_COUNT,
}