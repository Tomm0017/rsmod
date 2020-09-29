package gg.rsmod.game.model.coroutine.suspendable.condition.impl

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.coroutine.suspendable.condition.SuspendableCondition
import gg.rsmod.game.model.entity.Pawn

/**
 * A [SuspendableCondition] that waits until [srcPawn]'s coordinates match [destTile]'s coordinates exactly before
 *  returing `true` with [canResume]
 *
 *  @param [srcPawn] The [Pawn] whose [tile] must match [destTile]
 *
 *  @param [destTile] The tile that is being compared to [srcPawn]
 */
class TileCondition(
        private val srcPawn: Pawn,
        private val destTile: Tile
): SuspendableCondition {

    override fun canResume(): Boolean = srcPawn.tile.sameAs(destTile)

    override fun toString(): String = "TileCondition(srcPawn=$srcPawn, destTile=$destTile)"
}