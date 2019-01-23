package gg.rsmod.game.model.region.update

import gg.rsmod.game.message.Message
import gg.rsmod.game.message.impl.PlayAreaSoundMessage
import gg.rsmod.game.model.entity.AreaSound

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayAreaSoundUpdate(override val type: EntityUpdateType,
                          override val entity: AreaSound) : EntityUpdate<AreaSound>(type, entity) {

    override fun toMessage(): Message = PlayAreaSoundMessage(((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7), entity.id,
            entity.radius, entity.volume, entity.delay)
}