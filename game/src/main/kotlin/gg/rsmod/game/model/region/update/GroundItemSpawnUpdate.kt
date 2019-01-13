package gg.rsmod.game.model.region.update

import gg.rsmod.game.message.Message
import gg.rsmod.game.message.impl.SpawnGroundItemMessage
import gg.rsmod.game.model.entity.GroundItem

/**
 * @author Tom <rspsmods@gmail.com>
 */
class GroundItemSpawnUpdate(override val type: EntityUpdateType,
                            override val entity: GroundItem) : EntityUpdate<GroundItem>(type, entity) {

    override fun toMessage(): Message = SpawnGroundItemMessage(entity.item, entity.amount, ((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7))
}