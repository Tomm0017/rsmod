package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpenComponentMessage(val parent: Int, val child: Int, val component: Int, val type: Int) : Message