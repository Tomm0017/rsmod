package gg.rsmod.game.model.region.update

import gg.rsmod.game.message.Message
import gg.rsmod.game.model.entity.Entity

/**
 * @author Tom <rspsmods@gmail.com>
 */
abstract class EntityUpdate<T: Entity>(open val type: EntityUpdateType, open val entity: T) {

    abstract fun toMessage(): Message
}