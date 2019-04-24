package gg.rsmod.game.message

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.service.log.LoggerService
import mu.KLogging

/**
 * A [MessageHandler] is responsible for executing [Message] logic on the
 * game-thread.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface MessageHandler<T : Message> {

    /**
     * Handles the [message] on the game-thread with the ability to read and write
     * to the [Client].
     */
    fun handle(client: Client, world: World, message: T)

    /**
     * A default method to log the handlers.
     */
    fun log(client: Client, format: String, vararg args: Any) {
        if (client.logPackets) {
            val message = String.format(format, *args)
            val logService = client.world.getService(LoggerService::class.java, searchSubclasses = true)
            if (logService != null) {
                logService.logPacket(client, message)
            } else {
                logger.trace(message)
            }
        }
    }

    companion object : KLogging()
}