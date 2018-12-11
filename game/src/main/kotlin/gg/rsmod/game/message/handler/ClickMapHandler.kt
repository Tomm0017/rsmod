package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClickMapMovementMessage
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMapHandler : MessageHandler<ClickMapMovementMessage> {

    override fun handle(client: Client, message: ClickMapMovementMessage) {
        log(client, "Click Map: x={}, z={}, type={}", message.x, message.z, message.movementType)

        client.teleport(Tile(message.x, message.z))
        client.interruptPlugins()

        val directions = Direction.values().filter { it != Direction.NONE }
        val matrix = client.world.collision.regions.getChunkForTile(client.world, client.tile).getMatrix(client.tile.height)
        val localX = client.tile.x % 8
        val localZ = client.tile.z % 8
        println("Flags: ${matrix.get(localX, localZ)}. Blocked:")
        directions.forEach { dir ->
            println("\t$dir: ${matrix.isBlocked(localX, localZ, dir, false)}")
        }
    }
}