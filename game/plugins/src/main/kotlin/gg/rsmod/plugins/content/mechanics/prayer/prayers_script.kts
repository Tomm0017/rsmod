package gg.rsmod.plugins.content.mechanics.prayer

on_player_pre_death {
    val retribution = player.hasPrayerIcon(PrayerIcon.RETRIBUTION)
    if (retribution) {
        // TODO: handle retribution
    }
}

on_player_death {
    Prayers.deactivateAll(player)
}

/**
 * Deactivate all prayers on bait out.
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
    val opt = player.getInteractingOption()
    Prayers.toggleQuickPrayers(player, opt)
}

/**
 * Select quick-prayer.
 */
on_button(interfaceId = 77, component = 4) {
    val slot = player.getInteractingSlot()
    val prayer = Prayer.values.firstOrNull { prayer -> prayer.quickPrayerSlot == slot } ?: return@on_button
    Prayers.selectQuickPrayer(this, prayer)
}

/**
 * Accept selected quick-prayer.
 */
on_button(interfaceId = 77, component = 5) {
    player.openInterface(InterfaceDestination.PRAYER)
}