package gg.rsmod.game.sync

import org.apache.logging.log4j.LogManager
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PhasedSynchronizationTask(private val phaser: Phaser, val task: SynchronizationTask) : SynchronizationTask {

    companion object {
        private val logger = LogManager.getLogger(PhasedSynchronizationTask::class.java)
    }

    override fun run() {
        try {
            task.run()
        } catch (e: Exception) {
            logger.error("Error with task ${this::class.java.simpleName}.", e)
        } finally {
            phaser.arriveAndDeregister()
        }
    }
}