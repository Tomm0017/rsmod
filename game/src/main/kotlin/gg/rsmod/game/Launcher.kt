package gg.rsmod.game

import java.nio.file.Paths

object Launcher {

    @JvmStatic
    fun main(args: Array<String>) {
        val server = Server()
        server.startServer(apiProps = Paths.get("./data/api.yml"))
        server.startGame(
                filestore = Paths.get("./data", "cache"),
                gameProps = Paths.get("./game.yml"),
                packets = Paths.get("./data", "packets.yml"),
                blocks = Paths.get("./data", "blocks.yml"),
                devProps = Paths.get("./dev-settings.yml"),
                args = args)
    }
}