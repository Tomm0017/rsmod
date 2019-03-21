package gg.rsmod.game.sync

import mu.KLogging
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PhasedSynchronizationTask(private val phaser: Phaser, val task: SynchronizationTask) : SynchronizationTask {

    override fun run() {
        try {
            task.run()
        } catch (e: Exception) {
            logger.error("Error with task ${this::class.java.simpleName}.", e)
        } finally {
            phaser.arriveAndDeregister()
        }
    }

    companion object: KLogging()
}