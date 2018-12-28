package gg.rsmod.util.concurrency

import org.apache.logging.log4j.LogManager
import java.util.concurrent.Phaser

class PhasedTask(private val phaser: Phaser, private val task: Runnable) : Runnable {

    companion object {
        private val logger = LogManager.getLogger(PhasedTask::class.java)
    }

    override fun run() {
        try {
            task.run()
        } catch (e: Exception) {
            logger.error("Error with task ${task::class.java.simpleName}.", e)
        } finally {
            phaser.arriveAndDeregister()
        }
    }
}