import gg.rsmod.plugins.osrs.api.ComponentPane
import gg.rsmod.plugins.osrs.api.helper.invokeScript
import gg.rsmod.plugins.osrs.api.helper.openComponent
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.setComponentUnderlay
import gg.rsmod.plugins.osrs.content.inter.options.OptionsTab

onButton(parent = OptionsTab.COMPONENT_ID, child = 100) {
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
        return@onButton
    }
    p.setComponentUnderlay(color = -1, transparency = -1)
    p.openComponent(component = 65, pane = ComponentPane.MAIN_SCREEN)
    p.invokeScript(2276, 2)
    p.invokeScript(733, 0, 0, 0, 0, 0, 0, 0, 0)
}