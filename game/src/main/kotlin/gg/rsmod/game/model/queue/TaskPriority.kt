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
     * A standard queue task will execute immediately. If you have other tasks
     * pending, they will wait until this priority completes first and then continue.
     *
     * This priority type is cancelled when a player clicks away.
     */
    STANDARD,

    /**
     * A strong queue task will terminate all previous tasks and execute
     * immediately.
     *
     * This priority type is cancelled when a player clicks away.
     */
    STRONG
}