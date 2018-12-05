package gg.rsmod.game.model.entity

import gg.rsmod.game.model.*

/**
 * A controllable character in the world that is used by something, or someone,
 * for their own purpose.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class Pawn(open val world: World) : Entity() {

    /**
     * The index assigned when this [Pawn] is successfully added to a [PawnList].
     */
    var index = -1

    /**
     * The current 3D [Tile] that this [Pawn] is standing on in the [World].
     */
    var tile = Tile(0, 0)

    /**
     * The 3D [Tile] that this [Pawn] was standing on, in the last game cycle.
     */
    var lastTile: Tile? = null

    /**
     * The current directions that this [Pawn] is moving.
     */
    var step: Step? = null

    /**
     * The current [LockState] which filters what actions this [Pawn] can perform.
     */
    var lock = LockState.NONE

    /**
     * The attributes attached to the [Pawn].
     *
     * @see AttributeSystem
     */
    val attr = AttributeSystem()

    /**
     * Handles logic before any synchronization tasks are executed.
     */
    abstract fun cycle()
}
