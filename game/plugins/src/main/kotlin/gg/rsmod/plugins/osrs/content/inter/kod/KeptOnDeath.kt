package gg.rsmod.plugins.osrs.content.inter.kod

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.ComponentPane
import gg.rsmod.plugins.osrs.api.helper.*
import java.text.DecimalFormat

/**
 * @author Tom <rspsmods@gmail.com>
 */
object KeptOnDeath {

    const val COMPONENT_ID = 4

    fun open(p: Player) {
        val deathContainers = p.calculateDeathContainers()
        val keptContainer = deathContainers.kept
        val lostContainer = deathContainers.lost

        p.sendContainer(key = 584, container = keptContainer)
        p.sendContainer(key = 468, container = lostContainer)
        p.setComponentUnderlay(color = -1, transparency = -1)
        p.openComponent(component = 4, pane = ComponentPane.MAIN_SCREEN)
        p.invokeScript(118, 0, "", keptContainer.getOccupiedSlotCount(), 0, 0, "${DecimalFormat().format(lostContainer.networth(p.world))} gp")
    }
}