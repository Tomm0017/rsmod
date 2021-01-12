package gg.rsmod.plugins.content.mechanics.prayer

/**
 * Checks if Retribution is active and deals
 * ((prayer lvl)/4) damage to nearby enemies
 */
on_player_pre_death {
    if (Prayers.isActive(player, Prayer.RETRIBUTION)) {
        val targets = player.getNeighbourPawns()
        loop@ for (target in targets) {
            when {
                !target.lock.canBeAttacked() -> continue@loop
                (target is Player && !target.inWilderness()) -> continue@loop // TODO Check Duel Arena, Castle Wars, Clan Wars
                target.isDead() -> continue@loop
                target is Player || (target is Npc && target.def.isAttackable()) -> {
                    player.graphic(437)
                    target.hit(damage = (player.getSkills().getCurrentLevel(Skills.PRAYER) / 4))
                }
            }
        }
    }
}

on_player_death {
    Prayers.deactivateAll(player)
}

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
    player.timers[Prayers.PRAYER_DRAIN] = 1
    Prayers.drainPrayer(player)
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