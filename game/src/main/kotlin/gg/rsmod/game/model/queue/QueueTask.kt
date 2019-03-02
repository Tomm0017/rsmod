package gg.rsmod.game.model.queue

import gg.rsmod.game.plugin.Plugin
import kotlin.coroutines.Continuation

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class QueueTask(val priority: QueueTaskPriority, val plugin: Plugin, val continuation: Continuation<Unit>) {

    var invoked = false

}