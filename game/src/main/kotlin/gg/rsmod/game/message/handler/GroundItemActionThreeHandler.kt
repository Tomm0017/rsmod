package gg.rsmod.game.message.handler

import gg.rsmod.game.action.GroundItemTakeAction
import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.GroundItemActionThreeMessage
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.INTERACTING_GROUNDITEM_ATTR
import gg.rsmod.game.model.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
class GroundItemActionThreeHandler : MessageHandler<GroundItemActionThreeMessage> {

    override fun handle(client: Client, message: GroundItemActionThreeMessage) {
        /**
         * If player can't move, we don't do anything.
         */
        if (!client.lock.canMove()) {
            return
        }

        /**
         * If tile is too far away, don't process it.
         */
        val tile = Tile(message.x, message.z, client.tile.height)
        if (!tile.viewableFrom(client.tile, Player.TILE_VIEW_DISTANCE)) {
            return
        }

        /**
         * Get the region chunk that the object would belong to.
         */
        val chunk = client.world.chunks.getForTile(tile)
        val item = chunk.getEntities<GroundItem>(tile, EntityType.GROUND_ITEM).firstOrNull { it.item == message.item && it.canBeViewedBy(client) }

        /**
         * [GroundItem] doesn't exist in the region.
         */
        if (item == null) {
            logAntiCheat(client, "Ground item action 3: id=%d, x=%d, z=%d, movement=%d", message.item, message.x, message.z, message.movementType)
            return
        }

        log(client, "Ground item action 3: id=%d, x=%d, z=%d, movement=%d", message.item, message.x, message.z, message.movementType)

        client.interruptPlugins()
        client.resetInteractions()

        client.attr.put(INTERACTING_OPT_ATTR, 3)
        client.attr.put(INTERACTING_GROUNDITEM_ATTR, item)
        client.world.pluginExecutor.execute(client, GroundItemTakeAction.walkPlugin)
    }
}