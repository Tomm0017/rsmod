package gg.rsmod.game.model.container

/**
 * Represents a type of [ItemContainer] stack type.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class ContainerStackType {

    /**
     * Represents an [ItemContainer] which only stacks items that are defined to
     * be stackable by their metadata.
     */
    NORMAL,

    /**
     * Represents an [ItemContainer] which stacks the same items on top of one another,
     * if possible, even if it contradicts their metadata.
     */
    STACK,

    /**
     * Represents an [ItemContainer] which never stacks any item.
     */
    NO_STACK;
}