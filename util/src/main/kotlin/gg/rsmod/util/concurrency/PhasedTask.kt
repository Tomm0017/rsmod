package gg.rsmod.util.concurrency

import mu.KLogging
import java.util.concurrent.Phaser

class PhasedTask(private val phaser: Phaser, private val task: () -> Unit) : Runnable {

    override fun run() {
        try {
            task()
        } catch (e: Exception) {
            logger.error("Error with phased task.", e)
        } finally {
            phaser.arriveAndDeregister()
        }
    }

    companion object: KLogging()
}