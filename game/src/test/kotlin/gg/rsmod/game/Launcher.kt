package gg.rsmod.game

import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.path.PathfindingStrategy
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.service.GameService
import java.nio.file.Paths

fun main(args: Array<String>) {

    val server = Server()
    server.startServer(apiProps = Paths.get("./data/api.yml"))

    val world = server.startGame(
            filestore = Paths.get("./data", "cache"),
            gameProps = Paths.get("./game.yml"),
            packets = Paths.get("./data", "packets.yml"),
            blocks = Paths.get("./data", "blocks.yml"),
            devProps = Paths.get("./dev-settings.yml"))

    val gameService = world.getService(GameService::class.java, false).get()
    for (i in 0 until 1998) {
        val player = Player(world)
        player.username = "Test $i"
        player.tile = Tile(gameService.world.gameContext.home)
        /*player.register()

        player.world.pluginExecutor.execute(player) {
            it.suspendable {
                walkPlugin(it)
            }
        }*/
    }
}

suspend fun walkPlugin(it: Plugin) {
    val start = Tile(it.player().tile)
    while (true) {
        it.wait(10 + it.player().world.random(0..25))
        var randomX = it.player().tile.x + (-6 + it.player().world.random(0..12))
        var randomZ = it.player().tile.z + (-6 + it.player().world.random(0..12))
        if (!start.isWithinRadius(Tile(randomX, randomZ), PathfindingStrategy.MAX_DISTANCE - 1)) {
            randomX = start.x
            randomZ = start.z
        }
        it.player().walkTo(randomX, randomZ, MovementQueue.StepType.FORCED_RUN)
    }
}