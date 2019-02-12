package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.item.Item

/**
 * @author Tom <rspsmods@gmail.com>
 */
class GroundItem private constructor(val item: Int, var amount: Int, val owner: Any?) : Entity() {

    companion object {
        const val DEFAULT_RESPAWN_CYCLES = 50

        /**
         * The amount of cycles for this item to be publicly visible.
         */
        const val DEFAULT_PUBLIC_SPAWN_CYCLES = 100

        /**
         * The amount of cycles for this item to despawn from the world.
         */
        const val DEFAULT_DESPAWN_CYCLES = 500
    }

    constructor(item: Int, amount: Int, tile: Tile, owner: Any? = null) : this(item, amount, owner) {
        this.tile = tile
    }

    constructor(item: Item, tile: Tile, owner: Any? = null) : this(item.id, item.amount, tile, owner)

    var respawnCycles = -1

    var currentCycle = 0

    override fun getType(): EntityType = EntityType.GROUND_ITEM

    fun isOwnedBy(p: Player): Boolean = owner != null && p.uid == owner

    fun isOwnedBy(uid: Any?): Boolean = owner != null && uid == owner

    fun isPublic(): Boolean = owner == null

    fun canBeViewedBy(p: Player): Boolean = isPublic() || isOwnedBy(p)

    override fun toString(): String = MoreObjects.toStringHelper(this).add("item", item).add("amount", amount).add("tile", tile.toString()).add("owner", owner).toString()
}