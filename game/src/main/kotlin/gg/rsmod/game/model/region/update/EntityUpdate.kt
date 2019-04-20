package gg.rsmod.game.model.region.update

import com.google.common.base.MoreObjects
import gg.rsmod.game.message.Message
import gg.rsmod.game.model.entity.Entity

/**
 * Represents an update for an [Entity], in which they can be spawned
 * or removed from a client's viewport.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class EntityUpdate<T : Entity>(open val type: EntityUpdateType, open val entity: T) {

    abstract fun toMessage(): Message

    override fun toString(): String = MoreObjects.toStringHelper(this).add("type", type).add("entity", entity).toString()
}