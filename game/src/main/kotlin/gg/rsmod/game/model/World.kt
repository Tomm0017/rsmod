package gg.rsmod.game.model

import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.game.map.Map
import gg.rsmod.game.model.entity.PawnList
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.PluginExecutor
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * The game world, which stores all the entities and nodes that the world
 * needs to keep track of.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class World(val server: Server, val gameContext: GameContext) {

    val players = PawnList<Player>(gameContext.playerLimit)

    val maps = Map()

    /**
     * The [PluginExecutor] is responsible for executing plugins as requested by
     * the game.
     */
    val pluginExecutor = PluginExecutor()

    /**
     * A [Random] implementation used for pseudo-random purposes through-out
     * the game world.
     */
    private val random: Random = ThreadLocalRandom.current()

    /**
     * The amount of game cycles that have gone by since the world was first
     * initialized. This can reset back to [0], if it's signalled to overflow
     * any time soon.
     */
    var currentCycle = 0

    fun register(p: Player): Boolean {
        val registered = players.add(p)
        if (registered) {
            p.attr.put(INDEX_ATTR, p.index)
            return true
        }
        return false
    }

    fun unregister(p: Player) {
        maps.removePlayer(p)
        players.remove(p)
    }

    fun getPlayerForName(username: String): Optional<Player> {
        for (i in 0 until players.capacity) {
            val player = players.get(i) ?: continue
            if (player.username == username) {
                return Optional.of(player)
            }
        }
        return Optional.empty()
    }

    fun getNpcCount(): Int = 0

    fun random(range: IntRange): Int = random.nextInt(range.endInclusive - range.start + 1) + range.start
}