package gg.rsmod.game.model.region.update

import gg.rsmod.game.message.Message
import gg.rsmod.game.message.impl.SpawnObjectMessage
import gg.rsmod.game.model.entity.GameObject

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjectSpawnUpdate(override val type: EntityUpdateType,
                        override val entity: GameObject) : EntityUpdate<GameObject>(type, entity) {

    override fun toMessage(): Message = SpawnObjectMessage(entity.id, entity.settings.toInt(), ((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7))
}