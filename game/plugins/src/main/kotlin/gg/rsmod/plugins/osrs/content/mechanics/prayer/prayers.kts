import gg.rsmod.plugins.osrs.api.InterfaceDestination
import gg.rsmod.plugins.osrs.api.ext.getInteractingOption
import gg.rsmod.plugins.osrs.api.ext.getInteractingSlot
import gg.rsmod.plugins.osrs.api.ext.openInterface
import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.content.mechanics.prayer.Prayer
import gg.rsmod.plugins.osrs.content.mechanics.prayer.Prayers

/**
 * Deactivate all prayers on log out.
 */
onLogout {
    Prayers.deactivateAll(it.player())
}

/**
 * Activate prayers.
 */
Prayer.values.forEach { prayer ->
    onButton(parent = 541, child = prayer.child) {
        it.suspendable {
            Prayers.toggle(it, prayer)
        }
    }
}

/**
 * Prayer drain.
 */
onLogin {
    it.player().timers[Prayers.PRAYER_DRAIN] = 1
}

onTimer(Prayers.PRAYER_DRAIN) {
    val p = it.player()
    p.timers[Prayers.PRAYER_DRAIN] = 1
    Prayers.drainPrayer(p)
}

/**
 * Toggle quick-prayers.
 */
onButton(parent = 160, child = 14) {
    val p = it.player()
    val opt = it.getInteractingOption()
    Prayers.toggleQuickPrayers(p, opt)
}

/**
 * Select quick-prayer.
 */
onButton(parent = 77, child = 4) {
    val slot = it.getInteractingSlot()
    val prayer = Prayer.values.firstOrNull { prayer -> prayer.quickPrayerSlot == slot } ?: return@onButton
    Prayers.selectQuickPrayer(it, prayer)
}

/**
 * Accept selected quick-prayer.
 */
onButton(parent = 77, child = 5) {
    val p = it.player()
    p.openInterface(InterfaceDestination.PRAYER)
}