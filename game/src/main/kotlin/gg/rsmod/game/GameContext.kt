package gg.rsmod.game

import gg.rsmod.game.model.Tile

/**
 * Holds vital information that the game needs in order to run (properly).
 *
 * @param initialLaunch a flag which indicates whether this game launch is
 * the first ever launch.
 *
 * @param name the game name.
 *
 * @param revision
 * The cache revision the server is currently loading.
 *
 * @param cycleTime the time, in milliseconds, in between each full game
 * cycle/tick.
 *
 * @param playerLimit the max amount of players that can be online in the
 * world at once.
 *
 * @param home the [Tile] that will be used as the home area and tile where
 * new players will start off.
 *
 * @param skillCount the max amount of skills for players.
 *
 * @param npcStatCount the stat count for npcs.
 *
 * @param runEnergy if players' run energy will be deducted whilst running.
 *
 * @param gItemPublicDelay the amount of cycles for a [gg.rsmod.game.model.entity.GroundItem]
 * to become public if it's owned by a player.
 *
 * @param gItemDespawnDelay the amount of cycles for a [gg.rsmod.game.model.entity.GroundItem]
 * to despawn.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class GameContext(var initialLaunch: Boolean, val name: String, val revision: Int,
                       val cycleTime: Int, val playerLimit: Int, val home: Tile,
                       val skillCount: Int, val npcStatCount: Int, val runEnergy: Boolean,
                       val gItemPublicDelay: Int, val gItemDespawnDelay: Int)