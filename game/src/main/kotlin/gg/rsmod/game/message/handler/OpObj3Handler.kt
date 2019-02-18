package gg.rsmod.game.message.handler

import gg.rsmod.game.action.GroundItemTakeAction
import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpObj3Message
import gg.rsmod.game.model.*
import gg.rsmod.game.model.attr.INTERACTING_GROUNDITEM_ATTR
import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.priv.Privilege
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpObj3Handler : MessageHandler<OpObj3Message> {

    override fun handle(client: Client, message: OpObj3Message) {
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
        val chunk = client.world.chunks.getOrCreate(tile)
        val item = chunk.getEntities<GroundItem>(tile, EntityType.GROUND_ITEM).firstOrNull { it.item == message.item && it.canBeViewedBy(client) }

        /**
         * [GroundItem] doesn't exist in the region.
         */
        if (item == null) {
            logVerificationFail(client, "Ground item action 3: id=%d, x=%d, z=%d, movement=%d", message.item, message.x, message.z, message.movementType)
            return
        }

        if (message.movementType == 1 && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.teleport(item.tile)
        }

        client.attr[INTERACTING_OPT_ATTR] = 3
        client.attr[INTERACTING_GROUNDITEM_ATTR] = WeakReference(item)

        client.interruptPlugins()
        client.resetInteractions()
        client.world.pluginExecutor.execute(client, GroundItemTakeAction.walkPlugin)
    }
}