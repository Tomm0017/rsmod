package gg.rsmod.plugins.content.mechanics.ships

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.closeInterface
import gg.rsmod.plugins.api.ext.openInterface

object SailingInterface {
    /***
     * Tab Interface destinations which be affected by enableTabs/ disableTabs functions
     */
    private val interfaceDestination = setOf(
            InterfaceDestination.ATTACK, InterfaceDestination.SKILLS, InterfaceDestination.QUEST, InterfaceDestination.INVENTORY,
            InterfaceDestination.EQUIPMENT, InterfaceDestination.PRAYER, InterfaceDestination.MAGIC, InterfaceDestination.LOG_OUT,
            InterfaceDestination.SETTINGS, InterfaceDestination.EMOTES, InterfaceDestination.MUSIC)

    /**
     * restores tab interfaces to their default status.
     */
    fun enableTabs(player: Player) {
        interfaceDestination.forEach { interDst ->
            player.openInterface(interfaceId = interDst.interfaceId, dest = interDst)
        }
    }
    /**
     * disables tab interfaces.
     */
    fun disableTabs(player: Player) {
        interfaceDestination.forEach { interDst ->
            player.closeInterface(dest = interDst)
        }
    }
}