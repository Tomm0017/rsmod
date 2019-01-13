package gg.rsmod.game.model.region.update

import gg.rsmod.game.message.Message
import gg.rsmod.game.message.impl.RemoveGroundItemMessage
import gg.rsmod.game.model.entity.GroundItem

/**
 * @author Tom <rspsmods@gmail.com>
 */
class GroundItemRemoveUpdate(override val type: EntityUpdateType,
                             override val entity: GroundItem) : EntityUpdate<GroundItem>(type, entity) {

    override fun toMessage(): Message = RemoveGroundItemMessage(entity.item, ((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7))
}