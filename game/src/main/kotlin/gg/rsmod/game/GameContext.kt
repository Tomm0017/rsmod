package gg.rsmod.game

import gg.rsmod.game.model.PrivilegeSet
import gg.rsmod.game.model.Tile

/**
 * Holds vital information that the game needs in order to run (properly).
 *
 * @param name
 * The game name.
 *
 * @param revision
 * The cache revision the server is currently loading.
 *
 * @param cycleTime
 * The time, in milliseconds, in between each full game cycle/tick.
 *
 * @param playerLimit
 * The max amount of players that can be online in the world at once.
 *
 * @param home
 * The [Tile] that will be used as the home area and tile where new players
 * will start off.
 *
 * @param rsaEncryption
 * If RSA encryption should be used for encrypting/decrypting
 * [gg.rsmod.net.message.Message]s.
 *
 * @param devMode
 * This flag enables certain developer-only features, such as in-game debug
 * messages.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class GameContext(val name: String, val revision: Int, val cycleTime: Int, val playerLimit: Int,
                       val home: Tile, val rsaEncryption: Boolean, val devMode: Boolean) {

    /**
     * The [PrivilegeSet] that is attached to our game.
     */
    val privileges = PrivilegeSet()
}