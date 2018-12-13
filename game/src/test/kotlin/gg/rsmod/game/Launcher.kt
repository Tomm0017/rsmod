package gg.rsmod.game

import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.Tile
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.service.GameService
import java.nio.file.Paths

fun main(args: Array<String>) {
    val server = Server()

    server.startServer(apiProps = Paths.get("./data/api.yml"))
    val world = server.startGame(filestorePath = Paths.get("./data", "cache"), gameProps = Paths.get("./game.yml"),
                packets = Paths.get("./data/packets.yml"))

    val gameService = world.getService(GameService::class.java, false).get()
    /*for (i in 0 until 1997) {
        val player = Player(world)
        player.username = "Test $i"
        player.tile = Tile(gameService.world.gameContext.home)
        player.register()

        player.world.pluginExecutor.execute(player) {
            it.suspendable {
                testSuspend(it)
            }
        }
    }*/
}

suspend fun testSuspend(it: Plugin) {
    val start = Tile(it.player().tile)
    while (true) {
        it.wait(10 + it.player().world.random(0..25))
        val randomX = start.x + (-8 + it.player().world.random(0..16))
        val randomZ = start.z + (-8 + it.player().world.random(0..16))
        it.player().walkTo(randomX, randomZ, MovementQueue.StepType.FORCED_RUN)
    }
}