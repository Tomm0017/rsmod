package gg.rsmod.game.model.coroutine.suspendable.condition.impl

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.coroutine.suspendable.condition.SuspendableCondition

/**
 * A [SuspendableCondition] that waits until [srcTile]'s coordinates match [destTile]'s coordinates exactly before
 *  returing `true` with [canResume]
 *
 *  @param [srcTile] The tile that must match [destTile]
 *
 *  @param [destTile] The tile that is being compared to [srcTile]
 */
class TileCondition(
        private val srcTile: Tile,
        private val destTile: Tile
): SuspendableCondition {

    override fun canResume(): Boolean = srcTile.sameAs(destTile)

    override fun toString(): String = "TileCondition(srcTile=$srcTile, destTile=$destTile)"
}
