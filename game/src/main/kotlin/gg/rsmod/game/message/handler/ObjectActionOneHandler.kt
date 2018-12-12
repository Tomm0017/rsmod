package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ObjectActionOneMessage
import gg.rsmod.game.message.impl.SendChatboxTextMessage
import gg.rsmod.game.model.INTERACTING_OBJ_ATTR
import gg.rsmod.game.model.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.entity.EntityType
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.plugin.Plugin

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
        if (!tile.viewableFrom(client.tile)) {
            return
        }

        /**
         * Get the region chunk that the object would belong to.
         */
        val chunk = client.world.regions.getChunkForTile(tile)
        val obj = chunk.getEntities<GameObject>(tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull { it.id == message.id }

        /**
         * [GameObject] doesn't exist in the region.
         */
        if (obj == null) {
            logAntiCheat(client, "Object Action 1: id={}, x={}, z={}, movement={}", message.id, message.x, message.z, message.movementType)
            return
        }

        log(client, "Object Action 1: id={}, x={}, z={}, movement={}", message.id, message.x, message.z, message.movementType)

        client.interruptPlugins()

        client.attr.put(INTERACTING_OPT_ATTR, 1)
        client.attr.put(INTERACTING_OBJ_ATTR, obj)
        client.world.pluginExecutor.execute(client, walkPlugin)
    }

    private val walkPlugin: (Plugin) -> Unit = {
        val p = it.player()

        val obj = p.attr[INTERACTING_OBJ_ATTR]
        val opt = p.attr[INTERACTING_OPT_ATTR]

        if (!p.world.plugins.executeObject(p, obj.id, opt)) {
            p.write(SendChatboxTextMessage(type = 0, message = "Nothing interesting happens.", username = null))
            if (p.world.gameContext.devMode) {
                val debug = "Unhandled object action: [opt=$opt, id=${obj.id}, x=${obj.tile.x}, z=${obj.tile.z}]"
                p.write(SendChatboxTextMessage(type = 0, message = debug, username = null))
            }
        }
    }
}