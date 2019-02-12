import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.content.inter.kod.KeptOnDeath

onButton(parent = 387, child = 21) {
    val p = it.player()
    if (!p.lock.canComponentInteract()) {
        return@onButton
    }
    KeptOnDeath.open(it.player())
}

onComponentClose(component = KeptOnDeath.COMPONENT_ID) {
    /**
     * Have to resend inventory when this interface is closed as it sent a 'fake'
     * inventory container to the tab area, which can mess up other tab area
     * interfaces such as equipment stats.
     */
    it.player().inventory.dirty = true
}