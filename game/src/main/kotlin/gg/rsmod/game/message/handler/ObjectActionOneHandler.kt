package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ObjectActionOneMessage
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.INTERACTING_OBJ_ATTR
import gg.rsmod.game.model.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.action.ObjectPathAction

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjectActionOneHandler : MessageHandler<ObjectActionOneMessage> {

    override fun handle(client: Client, message: ObjectActionOneMessage) {
        /**
         * If player can't move, we don't do anything.
         */
        if (!client.canMove()) {
            return
        }

        /**
         * If tile is too far away, don't process it.
         */
        val tile = Tile(message.x, message.z, client.tile.height)
        if (!tile.viewableFrom(client.tile, 24)) {
            return
        }

        /**
         * Get the region chunk that the object would belong to.
         */
        val chunk = client.world.chunks.getForTile(tile)
        val obj = chunk.getEntities<GameObject>(tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull { it.id == message.id }

        /**
         * [GameObject] doesn't exist in the region.
         */
        if (obj == null) {
            logAntiCheat(client, "Object Action 1: id=%d, x=%d, z=%d, movement=%d", message.id, message.x, message.z, message.movementType)
            return
        }

        log(client, "Object Action 1: id=%d, x=%d, z=%d, movement=%d", message.id, message.x, message.z, message.movementType)

        client.interruptPlugins()
        client.attr.put(INTERACTING_OPT_ATTR, 1)
        client.attr.put(INTERACTING_OBJ_ATTR, obj)
        if (!client.world.plugins.executeCustomPathingObject(client, obj.id)) {
            client.world.pluginExecutor.execute(client, ObjectPathAction.walkPlugin)
        }
    }
}