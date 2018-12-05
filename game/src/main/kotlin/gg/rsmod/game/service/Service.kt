package gg.rsmod.game.service

import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.util.ServerProperties

/**
 * Any service that should be initialized when our server is starting up.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class Service {

    /**
     * Called when the server is starting up.
     */
    @Throws(Exception::class)
    abstract fun init(server: Server, gameContext: GameContext, serviceProperties: ServerProperties)

    /**
     * Called when the server is shutting off.
     */
    abstract fun terminate(server: Server, gameContext: GameContext)
}