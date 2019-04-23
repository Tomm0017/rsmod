package gg.rsmod.game.service.log

import gg.rsmod.game.service.Service

/**
 * A [Service] responsible for logging messages when requested.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface LoggerService : Service {

    /**
     * Log an info message.
     */
    fun info(message: String)
}