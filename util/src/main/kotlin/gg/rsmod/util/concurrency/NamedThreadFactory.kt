package gg.rsmod.util.concurrency

import org.apache.logging.log4j.LogManager
import java.util.concurrent.ThreadFactory

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NamedThreadFactory {

    companion object {
        private val logger = LogManager.getLogger(NamedThreadFactory::class.java)
    }

    private var name: String? = null

    private var exceptionHandler: Thread.UncaughtExceptionHandler? = null

    fun setName(name: String): NamedThreadFactory {
        this.name = name
        return this
    }

    fun setExceptionHandler(exceptionHandler: Thread.UncaughtExceptionHandler): NamedThreadFactory {
        this.exceptionHandler = exceptionHandler
        return this
    }

    fun build(): ThreadFactory {
        val name = this.name ?: "unnamed-thread"
        val exceptionHandler = this.exceptionHandler ?:
        Thread.UncaughtExceptionHandler { _, e ->
            logger.error("Error with thread: $name", e)
        }

        return ThreadFactory { r ->
            val thread = Thread(r, name)
            thread.uncaughtExceptionHandler = exceptionHandler
            thread
        }
    }
}