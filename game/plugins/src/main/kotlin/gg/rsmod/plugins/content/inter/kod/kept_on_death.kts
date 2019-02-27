package gg.rsmod.plugins.content.inter.kod

import gg.rsmod.plugins.api.ext.player

on_button(interfaceId = 387, component = 21) {
    val p = player
    if (!p.lock.canInterfaceInteract()) {
        return@on_button
    }
    KeptOnDeath.open(player)
}

on_interface_close(interfaceId = KeptOnDeath.COMPONENT_ID) {
    /**
     * Have to resend inventory when this interface is closed as it sent a 'fake'
     * inventory container to the tab area, which can mess up other tab area
     * interfaces such as equipment stats.
     */
    player.inventory.dirty = true
}