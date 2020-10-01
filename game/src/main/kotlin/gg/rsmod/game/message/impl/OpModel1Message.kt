package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message


/**
 * This happens when you click a model component
 *
 * @property [componentId] The component id of this model inside the current interface
 *
 * @author Curtis Woodard <nbness1337@gmail.com>
 */
data class OpModel1Message(val componentId: Int): Message