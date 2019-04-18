package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.PlayerUID
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.item.Item

/**
 * An item that is spawned on the ground.
 *
 * @param ownerUID
 * If null, the item will be visible and can be interacted with by any player
 * in the world. Otherwise, it will only be visible to the player who's [Player.uid]
 * matches [ownerUID].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class GroundItem private constructor(val item: Int, var amount: Int, internal var ownerUID: PlayerUID?) : Entity() {

    constructor(item: Int, amount: Int, tile: Tile, owner: Player? = null) : this(item, amount, owner?.uid) {
        this.tile = tile
    }

    constructor(item: Item, tile: Tile, owner: Player? = null) : this(item.id, item.amount, tile, owner)

    internal var currentCycle = 0

    internal var respawnCycles = -1

    override val entityType: EntityType = EntityType.GROUND_ITEM

    fun isOwnedBy(p: Player): Boolean = ownerUID != null && p.uid.value == ownerUID!!.value

    fun isPublic(): Boolean = ownerUID == null

    fun canBeViewedBy(p: Player): Boolean = isPublic() || isOwnedBy(p)

    fun removeOwner() {
        ownerUID = null
    }

    override fun toString(): String = MoreObjects.toStringHelper(this).add("item", item).add("amount", amount).add("tile", tile.toString()).add("owner", ownerUID).toString()

    companion object {
        /**
         * The default amount of cycles for this ground item to respawn if flagged
         * to do so.
         */
        const val DEFAULT_RESPAWN_CYCLES = 50

        /**
         * The default amount of cycles for this item to be publicly visible.
         */
        const val DEFAULT_PUBLIC_SPAWN_CYCLES = 100

        /**
         * The default amount of cycles for this item to despawn from the world.
         */
        const val DEFAULT_DESPAWN_CYCLES = 600
    }
}
