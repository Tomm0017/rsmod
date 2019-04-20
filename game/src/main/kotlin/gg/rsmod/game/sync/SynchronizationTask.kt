package gg.rsmod.game.sync

import gg.rsmod.game.model.entity.Pawn

/**
 * A task in any pawn synchronization process.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface SynchronizationTask<T : Pawn> {

    fun run(pawn: T)
}