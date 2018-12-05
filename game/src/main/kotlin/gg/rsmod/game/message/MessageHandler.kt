package gg.rsmod.game.message

import gg.rsmod.game.model.entity.Client

/**
 * A [MessageHandler] is responsible for executing [Message] logic on the
 * game-thread.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface MessageHandler<T: Message> {

    /**
     * Handles the [message] on the game-thread with the ability to read and write
     * to the [Client].
     */
    fun handle(client: Client, message: T)

    /**
     * A default method to log the handlers.
     */
    fun log(client: Client, format: String, vararg args: Any) {
        // TODO: submit a log for db or however we want to log, should probably
        // only log packets for certain/suspicious players
    }
}