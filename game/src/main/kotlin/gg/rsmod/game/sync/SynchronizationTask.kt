package gg.rsmod.game.sync

/**
 * A task in any pawn synchronization process.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface SynchronizationTask : Runnable {

    override fun run()
}