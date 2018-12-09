package gg.rsmod.game.model.interf

import gg.rsmod.game.model.entity.Player

/**
 * A listener that can be used to specify logic that must be executed upon
 * certain interface actions.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface InterfaceActionListener {

    fun onOpen(p: Player, parent: Int, child: Int, interfaceId: Int, type: Int)

    fun onClose(p: Player, interfaceHash: Int)

    fun onDisplayChange(p: Player, newMode: DisplayMode)
}