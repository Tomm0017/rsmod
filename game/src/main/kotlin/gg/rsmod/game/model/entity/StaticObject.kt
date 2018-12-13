package gg.rsmod.game.model.entity

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile

/**
 * A [StaticObject] is a game object that is part of the static terrain in our
 * game (loaded from game resources).
 *
 * @author Tom <rspsmods@gmail.com>
 */
class StaticObject(id: Int, type: Int, rot: Int, tile: Tile) : GameObject(id, type, rot, tile) {

    override fun getType(): EntityType = EntityType.STATIC_OBJECT
}