package gg.rsmod.plugins.osrs.content.inter.kod

import gg.rsmod.plugins.osrs.api.ext.player

on_button(parent = 387, child = 21) {
    val p = it.player()
    if (!p.lock.canComponentInteract()) {
        return@on_button
    }
    KeptOnDeath.open(it.player())
}

on_interface_close(interfaceId = KeptOnDeath.COMPONENT_ID) {
    /**
     * Have to resend inventory when this interface is closed as it sent a 'fake'
     * inventory container to the tab area, which can mess up other tab area
     * interfaces such as equipment stats.
     */
    it.player().inventory.dirty = true
}