package gg.rsmod.game.model.region.update

import gg.rsmod.game.message.Message
import gg.rsmod.game.message.impl.MapAnimMessage
import gg.rsmod.game.model.Graphic
import gg.rsmod.game.model.TileGraphic

/**
 * Represents an update where a [Graphic] is spawned on a tile.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class MapAnimUpdate(override val type: EntityUpdateType,
                    override val entity: TileGraphic) : EntityUpdate<TileGraphic>(type, entity) {

    override fun toMessage(): Message = MapAnimMessage(entity.id, entity.height, entity.delay,
            ((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7))
}