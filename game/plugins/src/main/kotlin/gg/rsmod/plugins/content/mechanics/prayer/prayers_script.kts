package gg.rsmod.plugins.content.mechanics.prayer

/**
 * Deactivate all prayers on log out.
 */
on_logout {
    Prayers.deactivateAll(player)
}

/**
 * Activate prayers.
 */
Prayer.values.forEach { prayer ->
    on_button(interfaceId = 541, component = prayer.child) {
        player.queue {
            Prayers.toggle(this, prayer)
        }
    }
}

/**
 * Prayer drain.
 */
on_login {
    player.timers[Prayers.PRAYER_DRAIN] = 1
}

on_timer(Prayers.PRAYER_DRAIN) {
    val p = player
    p.timers[Prayers.PRAYER_DRAIN] = 1
    Prayers.drainPrayer(p)
}

/**
 * Toggle quick-prayers.
 */
on_button(interfaceId = 160, component = 14) {
    val p = player
    val opt = getInteractingOption()
    Prayers.toggleQuickPrayers(p, opt)
}

/**
 * Select quick-prayer.
 */
on_button(interfaceId = 77, component = 4) {
    val slot = getInteractingSlot()
    val prayer = Prayer.values.firstOrNull { prayer -> prayer.quickPrayerSlot == slot } ?: return@on_button
    Prayers.selectQuickPrayer(this, prayer)
}

/**
 * Accept selected quick-prayer.
 */
on_button(interfaceId = 77, component = 5) {
    val p = player
    p.openInterface(InterfaceDestination.PRAYER)
}