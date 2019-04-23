package gg.rsmod.game.service.log

import gg.rsmod.game.service.Service

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface LoggerService : Service {

    fun info(message: String)
}