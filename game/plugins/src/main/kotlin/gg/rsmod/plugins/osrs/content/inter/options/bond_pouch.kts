package gg.rsmod.plugins.osrs.content.inter.options

import gg.rsmod.plugins.osrs.api.InterfaceDestination
import gg.rsmod.plugins.osrs.api.ext.openInterface
import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.api.ext.runClientScript
import gg.rsmod.plugins.osrs.api.ext.setInterfaceUnderlay

on_button(parent = OptionsTab.INTERFACE_ID, child = 100) {
    /**
    [Varp (SHORT)]: id=1780, state=6
    [Varp (SHORT)]: id=1781, state=4
    [Varp (SHORT)]: id=1795, state=0
    [Invoke Script]: [2498, 1, 1, 1, 0]
    [Invoke Script]: [2524, -1, -1]
    [Open Widget]: widgetId=65, onWidget=35913749 (548, 21), clickThrough=0
    [Invoke Script]: [2276, 2]
    [Invoke Script]: [733, 0, 0, 0, 0, 0, 0, 0, 0]
     */
    val p = it.player()
    if (!p.lock.canComponentInteract()) {
        return@on_button
    }
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 65, dest = InterfaceDestination.MAIN_SCREEN)
    p.runClientScript(2276, 2)
    p.runClientScript(733, 0, 0, 0, 0, 0, 0, 0, 0)
}