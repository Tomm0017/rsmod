package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ComponentSwitchMessage(val from: Int, val to: Int) : Message