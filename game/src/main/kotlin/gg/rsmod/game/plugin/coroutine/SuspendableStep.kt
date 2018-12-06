package gg.rsmod.game.plugin.coroutine

import kotlin.coroutines.Continuation

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class SuspendableStep(val condition: SuspendableCondition, val continuation: Continuation<Unit>)