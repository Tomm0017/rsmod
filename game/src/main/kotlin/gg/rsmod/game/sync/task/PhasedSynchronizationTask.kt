package gg.rsmod.game.sync.task

import org.apache.logging.log4j.LogManager
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PhasedSynchronizationTask(open val phaser: Phaser) : Runnable {

    companion object {
        private val logger = LogManager.getLogger(PhasedSynchronizationTask::class.java)
    }

    abstract fun execute()

    final override fun run() {
        try {
            execute()
        } catch (e: Exception) {
            logger.error("Error with task ${this::class.java.simpleName}.", e)
        } finally {
            phaser.arriveAndDeregister()
        }
    }
}