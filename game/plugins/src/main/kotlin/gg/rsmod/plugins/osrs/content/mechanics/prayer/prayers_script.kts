package gg.rsmod.plugins.osrs.content.mechanics.prayer

import gg.rsmod.plugins.osrs.api.InterfaceDestination
import gg.rsmod.plugins.osrs.api.ext.getInteractingOption
import gg.rsmod.plugins.osrs.api.ext.getInteractingSlot
import gg.rsmod.plugins.osrs.api.ext.openInterface
import gg.rsmod.plugins.osrs.api.ext.player

/**
 * Deactivate all prayers on log out.
 */
on_logout {
    Prayers.deactivateAll(it.player())
}

/**
 * Activate prayers.
 */
Prayer.values.forEach { prayer ->
    on_button(interfaceId = 541, component = prayer.child) {
        it.suspendable {
            Prayers.toggle(it, prayer)
        }
    }
}

/**
 * Prayer drain.
 */
on_login {
    it.player().timers[Prayers.PRAYER_DRAIN] = 1
}

on_timer(Prayers.PRAYER_DRAIN) {
    val p = it.player()
    p.timers[Prayers.PRAYER_DRAIN] = 1
    Prayers.drainPrayer(p)
}

/**
 * Toggle quick-prayers.
 */
on_button(interfaceId = 160, component = 14) {
    val p = it.player()
    val opt = it.getInteractingOption()
    Prayers.toggleQuickPrayers(p, opt)
}

/**
 * Select quick-prayer.
 */
on_button(interfaceId = 77, component = 4) {
    val slot = it.getInteractingSlot()
    val prayer = Prayer.values.firstOrNull { prayer -> prayer.quickPrayerSlot == slot } ?: return@on_button
    Prayers.selectQuickPrayer(it, prayer)
}

/**
 * Accept selected quick-prayer.
 */
on_button(interfaceId = 77, component = 5) {
    val p = it.player()
    p.openInterface(InterfaceDestination.PRAYER)
}