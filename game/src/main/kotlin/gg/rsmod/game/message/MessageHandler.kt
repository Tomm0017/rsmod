package gg.rsmod.game.message

import gg.rsmod.game.model.entity.Client
import org.apache.logging.log4j.LogManager

/**
 * A [MessageHandler] is responsible for executing [Message] logic on the
 * game-thread.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface MessageHandler<T: Message> {

    companion object {
        private val logger = LogManager.getLogger(MessageHandler::class.java)
    }

    /**
     * Handles the [message] on the game-thread with the ability to read and write
     * to the [Client].
     */
    fun handle(client: Client, message: T)

    /**
     * A default method to log the handlers.
     */
    fun log(client: Client, format: String, vararg args: Any) {
        // TODO: log normal packets if applicable (maybe have a toggle for certain
        // flagged/suspicious players to be logged)
    }

    fun logAntiCheat(client: Client, format: String, vararg args: Any) {
        // TODO: log that someone is trying to cheat, though not succeeding.
    }
}