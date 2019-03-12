package gg.rsmod.game.model.queue

/**
 * Represents a priority type for a [QueueTask].
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class TaskPriority {

    /**
     * A weak priority task is cancelled if the player clicks away.
     */
    WEAK,

    /**
     * A standard priority task will wait if you have a menu open,
     * and execute when said menu closes.
     */
    STANDARD,

    /**
     * A strong priority task will close menus to execute itself sooner.
     */
    STRONG
}