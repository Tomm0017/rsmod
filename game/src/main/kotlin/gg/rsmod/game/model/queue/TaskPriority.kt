package gg.rsmod.game.model.queue

/**
 * Represents a priority type for a [QueueTask].
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class TaskPriority {

    /**
     * A weak queue task can be cancelled when a player clicks away.
     */
    WEAK,

    /**
     * A standard queue task will wait if you have a other tasks and execute
     * once they complete and will cancel when a player clicks away.
     */
    STANDARD,

    /**
     * A strong queue task will terminate all previous tasks and execute
     * immediately and will cancel when a player clicks away.
     */
    STRONG
}