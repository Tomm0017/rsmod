package gg.rsmod.util.concurrency

import mu.KotlinLogging
import java.util.concurrent.Phaser

class PhasedTask(private val phaser: Phaser, private val task: Runnable) : Runnable {

    companion object {
        private val logger = KotlinLogging.logger {  }
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