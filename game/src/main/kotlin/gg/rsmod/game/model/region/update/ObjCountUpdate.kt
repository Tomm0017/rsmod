package gg.rsmod.game.model.region.update

import gg.rsmod.game.message.Message
import gg.rsmod.game.message.impl.ObjCountMessage
import gg.rsmod.game.model.entity.GroundItem

/**
 * Represents an update where a [GroundItem]'s [GroundItem.amount] is changed.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ObjCountUpdate(override val type: EntityUpdateType, override val entity: GroundItem,
                     private val oldAmount: Int, private val newAmount: Int) : EntityUpdate<GroundItem>(type, entity) {

    override fun toMessage(): Message = ObjCountMessage(entity.item, oldAmount, newAmount, ((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7))
}