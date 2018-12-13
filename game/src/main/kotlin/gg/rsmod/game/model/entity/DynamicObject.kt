package gg.rsmod.game.model.entity

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile

/**
 * A [DynamicObject] is a game object that can be spawned by the [gg.rsmod.game.model.World].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class DynamicObject(id: Int, type: Int, rot: Int, tile: Tile) : GameObject(id, type, rot, tile) {

    override fun getType(): EntityType = EntityType.DYNAMIC_OBJECT
}