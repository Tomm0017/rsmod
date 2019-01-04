package gg.rsmod.game

import gg.rsmod.game.model.Tile

/**
 * Holds vital information that the game needs in order to run (properly).
 *
 * @param initialLaunch
 * A flag which indicates whether this game launch is the first ever launch.
 * See [first-launch] file in root directory for more information.
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
 * @param skillCount
 * The max amount of skills in our game.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class GameContext(var initialLaunch: Boolean, val name: String, val revision: Int,
                       val cycleTime: Int, val playerLimit: Int, val home: Tile,
                       val skillCount: Int, val runEnergy: Boolean) {


}