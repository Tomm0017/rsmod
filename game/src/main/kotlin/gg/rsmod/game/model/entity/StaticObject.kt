package gg.rsmod.game.model.entity

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile

/**
 * A [StaticObject] is a game object that is part of the static terrain loaded
 * from the game's resources.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class StaticObject(id: Int, type: Int, rot: Int, tile: Tile) : GameObject(id, type, rot, tile) {

    override val entityType: EntityType = EntityType.STATIC_OBJECT
}