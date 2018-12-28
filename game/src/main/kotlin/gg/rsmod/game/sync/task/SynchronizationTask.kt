package gg.rsmod.game.sync.task

/**
 * A task in any pawn synchronization process.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface SynchronizationTask : Runnable {

    override fun run()
}