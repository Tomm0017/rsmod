package gg.rsmod.game

import java.nio.file.Paths

fun main(args: Array<String>) {
    val server = Server()

    server.startServer(apiProps = Paths.get("./data/api.yml"))
    server.startGame(filestorePath = Paths.get("./data", "cache"), gameProps = Paths.get("./game.yml"),
            packets = Paths.get("./data/packets.yml"))

    /*val gameService = server.getService(GameService::class.java, false).get()
    val world = gameService.world
    for (i in 0 until 1997) {
        val player = Player(world)
        player.username = "Test $i"
        player.tile = Tile(gameService.world.gameContext.home).transform(world.random(-16..16), world.random(-16..16))
        player.register()
    }*/
}