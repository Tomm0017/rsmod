package gg.rsmod.game.model.entity

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World

/**
 * An [Entity] can be anything in the world that that maintains a [Tile].
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class Entity {

    companion object {
        const val NOTHING_INTERESTING_HAPPENS = "Nothing interesting happens."
        const val YOU_CANT_REACH_THAT = "I can't reach that!"
    }

    /**
     * The current 3D [Tile] that this [Pawn] is standing on in the [World].
     */
    var tile = Tile(0, 0)

    abstract fun getType(): EntityType
}