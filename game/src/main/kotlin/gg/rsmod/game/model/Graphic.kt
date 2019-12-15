package gg.rsmod.game.model

import gg.rsmod.game.model.entity.Entity

/**
 * Represents a graphic in the game world.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Graphic(val id: Int, val height: Int, var delay: Int = 0)

/**
 * A [Graphic] with a physical representation in the world.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class TileGraphic private constructor(val id: Int, val height: Int, val delay: Int = 0) : Entity() {

    constructor(tile: Tile, id: Int, height: Int, delay: Int = 0) : this(id, height, delay) {
        this.tile = tile
    }

    override val entityType: EntityType
        get() = EntityType.MAP_ANIM
}