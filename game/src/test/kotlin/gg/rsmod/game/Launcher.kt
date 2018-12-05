package gg.rsmod.game

import java.nio.file.Paths

fun main(args: Array<String>) {
    val server = Server()
    server.startServer(apiProps = Paths.get("./data/api.yml"))
    server.startGame(gameProps = Paths.get("./game.yml"), packets = Paths.get("./packets.yml"))

    /*val gameService = server.getService(GameService::class.java, false).get()
    val world = gameService.world
    for (i in 0 until 5) {
        val player = Player(world)
        player.username = "Test $i"
        player.tile = Tile(gameService.world.gameContext.home).transform(world.random(-4..4), world.random(-4..4))
        player.register()
    }*/

    /*game.world.players.forEach { p ->
        server.getPlugins().executeObject(p, 0, 1)
    }*/
}