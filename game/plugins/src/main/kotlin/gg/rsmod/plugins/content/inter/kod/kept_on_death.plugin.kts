package gg.rsmod.plugins.content.inter.kod

import gg.rsmod.plugins.api.EquipmentType.Companion.EQUIPMENT_INTERFACE_ID
import gg.rsmod.plugins.content.inter.kod.KeptOnDeath.KOD_INTERFACE_ID
import gg.rsmod.plugins.content.inter.kod.KeptOnDeath.KOD_COMPONENT_ID

on_button(interfaceId = EQUIPMENT_INTERFACE_ID, component = KOD_COMPONENT_ID) {
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }
    KeptOnDeath.open(player, world)
}

on_interface_close(interfaceId = KOD_INTERFACE_ID) {
    /**
     * Have to resend inventory when this interface is closed as it sent a 'fake'
     * inventory container to the tab area, which can mess up other tab area
     * interfaces such as equipment stats.
     */
    player.inventory.dirty = true
}