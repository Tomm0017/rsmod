package gg.rsmod.plugins.content.inter.kod

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*
import java.text.DecimalFormat

/**
 * @author Tom <rspsmods@gmail.com>
 */
object KeptOnDeath {

    const val COMPONENT_ID = 4

    fun open(p: Player, world: World) {
        val deathContainers = p.calculateDeathContainers()
        val keptContainer = deathContainers.kept
        val lostContainer = deathContainers.lost

        p.sendItemContainer(key = 584, container = keptContainer)
        p.sendItemContainer(key = 468, container = lostContainer)
        p.setInterfaceUnderlay(color = -1, transparency = -1)
        p.openInterface(interfaceId = 4, dest = InterfaceDestination.MAIN_SCREEN)
        p.runClientScript(118, 0, "", keptContainer.occupiedSlotCount, 0, 0, "${DecimalFormat().format(lostContainer.getNetworth(world))} gp")
    }
}