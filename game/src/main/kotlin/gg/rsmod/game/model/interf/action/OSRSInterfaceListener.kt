package gg.rsmod.game.model.interf.action

import gg.rsmod.game.message.impl.CloseInterfaceMessage
import gg.rsmod.game.message.impl.OpenInterfaceMessage
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.game.model.interf.InterfaceActionListener

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OSRSInterfaceListener : InterfaceActionListener {

    override fun onOpen(p: Player, parent: Int, child: Int, interfaceId: Int, type: Int) {
        p.write(OpenInterfaceMessage(parent, child, interfaceId, type))
    }

    override fun onClose(p: Player, interfaceHash: Int) {
        p.write(CloseInterfaceMessage(interfaceHash))
    }

    override fun onDisplayChange(p: Player, newMode: DisplayMode) = TODO("Implement")
}