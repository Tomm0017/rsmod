package gg.rsmod.plugins.content.inter.options

on_button(interfaceId = OptionsTab.INTERFACE_ID, component = 100) {
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
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }
    player.setInterfaceUnderlay(color = -1, transparency = -1)
    player.openInterface(interfaceId = 65, dest = InterfaceDestination.MAIN_SCREEN)
    player.runClientScript(2276, 2)
    player.runClientScript(733, 0, 0, 0, 0, 0, 0, 0, 0)
}