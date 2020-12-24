package gg.rsmod.plugins.content.inter.kod

import gg.rsmod.game.model.World
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*
import java.text.DecimalFormat

/**
 * @author Tom <rspsmods@gmail.com>
 */
object KeptOnDeath {
    const val KOD_INTERFACE_ID = 4
    const val KOD_COMPONENT_ID = 5

    fun open(p: Player, world: World) {
        val deathContainers = p.calculateDeathContainers()
        val keptContainer = deathContainers.kept
        val dummyContainer = ItemContainer(world.definitions, capacity = 50, stackType = ContainerStackType.NO_STACK)
        val lostContainer = deathContainers.lost

        dummyContainer.add(368, 9)
        dummyContainer.add(324, 41)

        p.sendItemContainer(interfaceId = -1, component = 230, key = 584, container = lostContainer)
        p.sendItemContainer(interfaceId = -1, component = 90, key = 468, container = dummyContainer)
        p.sendItemContainer(interfaceId = -1, component = 209, key = 93, container = keptContainer)
        p.setInterfaceUnderlay(color = -1, transparency = -1)
        p.openInterface(interfaceId = KOD_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
        p.runClientScript(972, 0, 0, 0, 0, "", if (p.getVarp(599)==1) 4 else 3, keptContainer[0]?.id ?: -1, keptContainer[1]?.id ?: -1, keptContainer[2]?.id ?: -1, if (p.getVarbit(599) == 1) keptContainer[3]!!.id else -1)
        p.setInterfaceEvents(interfaceId = KOD_INTERFACE_ID, component = 12, range = 0..3, setting = 1)
        p.setComponentText(KOD_INTERFACE_ID, 18, "Guide risk value:<br><col=ffffff>${DecimalFormat().format(lostContainer.getNetworth(world))}</col> gp")
    }
}