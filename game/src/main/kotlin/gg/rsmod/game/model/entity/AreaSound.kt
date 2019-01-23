package gg.rsmod.game.model.entity

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile

/**
 * @author Tom <rspsmods@gmail.com>
 */
class AreaSound private constructor(val id: Int, val radius: Int, val volume: Int, val delay: Int): Entity() {

    constructor(tile: Tile, id: Int, radius: Int, volume: Int, delay: Int = 0) : this(id, radius, volume, delay) {
        check(radius <= 0xf) { "Radius can not exceed 15 tiles." }
        this.tile = tile
    }

    override fun getType(): EntityType = EntityType.AREA_SOUND
}