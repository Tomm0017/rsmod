package gg.rsmod.game

import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.path.PathfindingStrategy
import gg.rsmod.game.plugin.Plugin
import java.nio.file.Paths

class Launcher {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            val server = Server()
            server.startServer(apiProps = Paths.get("../data/api.yml"))

            val world = server.startGame(
                    filestore = Paths.get("../data", "cache"),
                    gameProps = Paths.get("../game.yml"),
                    packets = Paths.get("../data", "packets.yml"),
                    blocks = Paths.get("../data", "blocks.yml"),
                    devProps = Paths.get("../dev-settings.yml"),
                    args = args)

            //val gameService = world.getService(GameService::class.java, false).orElse(null)

            for (i in 0 until 1998) {
                val player = Player(world)
                player.username = "Test $i"
                player.tile = Tile(world.gameContext.home).transform(world.random(-0..0), world.random(-0..0))

                /*gameService?.submitGameThreadJob {
                    player.register()
                    player.world.pluginExecutor.execute(player) {
                        it.suspendable {
                            walkPlugin(it)
                        }
                    }
                }*/
            }
        }

        private suspend fun walkPlugin(it: Plugin) {
            val p = it.ctx as Player

            val start = Tile(p.tile)
            while (true) {
                it.wait(10 + p.world.random(0..25))

                var randomX = p.tile.x + (-6 + p.world.random(0..12))
                var randomZ = p.tile.z + (-6 + p.world.random(0..12))
                if (!start.isWithinRadius(Tile(randomX, randomZ), PathfindingStrategy.MAX_DISTANCE - 1)) {
                    randomX = start.x
                    randomZ = start.z
                }
                p.walkTo(randomX, randomZ, MovementQueue.StepType.FORCED_RUN)
            }
        }
    }
}