package gg.rsmod.game.model

import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.game.map.Map
import gg.rsmod.game.model.entity.PawnList
import gg.rsmod.game.model.entity.Player
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
     * A [Random] implementation used for pseudo-random purposes through-out
     * the game world.
     */
    private val random: Random = ThreadLocalRandom.current()

    fun register(p: Player): Boolean {
        return players.add(p)
    }

    fun unregister(p: Player) {
        maps.removePlayer(p)
        players.remove(p)
    }

    fun getPlayerForName(username: String): Optional<Player> {
        for (i in 0 until players.capacity) {
            val player = players.get(i)
            if (player?.username == username) {
                return Optional.of(player)
            }
        }
        return Optional.empty()
    }

    fun getNpcCount(): Int = 0

    fun random(range: IntRange): Int = random.nextInt(range.endInclusive - range.start + 1) + range.start
}