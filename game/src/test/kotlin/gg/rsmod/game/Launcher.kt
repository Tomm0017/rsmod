package gg.rsmod.game

import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.path.PathfindingStrategy
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.model.Equipment
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

    for (i in 0 until 1998) {
        val player = Player(world)
        player.username = "Test $i"
        player.tile = Tile(world.gameContext.home).transform(world.random(-16..16), world.random(-16..16))
        //player.register()

        /*player.world.pluginExecutor.execute(player) {
            it.suspendable {
                walkPlugin(it)
            }
        }*/
    }
}

suspend fun walkPlugin(it: Plugin) {
    val p = it.ctx as Player

    val randomItems = arrayOf(
            hashMapOf(
                    Equipment.HEAD.id to Item(10828),
                    Equipment.CHEST.id to Item(11832),
                    Equipment.LEGS.id to Item(11834),
                    Equipment.BOOTS.id to Item(11836)
            ),
            hashMapOf(
                    Equipment.HEAD.id to Item(11826),
                    Equipment.CHEST.id to Item(11828),
                    Equipment.LEGS.id to Item(11830),
                    Equipment.BOOTS.id to Item(11840)
            ),
            hashMapOf(
                    Equipment.HEAD.id to Item(11665),
                    Equipment.CHEST.id to Item(8839),
                    Equipment.LEGS.id to Item(8840),
                    Equipment.BOOTS.id to Item(11840)
            )
    )

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
        /*it.wait(1)
        p.equipment.setItems(randomItems.random())
        p.addBlock(UpdateBlockType.APPEARANCE)*/
    }
}