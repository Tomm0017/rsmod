package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpenInterfaceMessage(val parent: Int, val child: Int, val interfaceId: Int, val type: Int) : Message