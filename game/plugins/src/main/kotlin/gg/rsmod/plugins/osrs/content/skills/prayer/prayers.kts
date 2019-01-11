
import gg.rsmod.game.model.Privilege
import gg.rsmod.plugins.osrs.api.*
import gg.rsmod.plugins.osrs.content.skills.prayer.Prayer
import gg.rsmod.plugins.osrs.content.skills.prayer.Prayers

/**
 * Infinite prayer command.
 */
r.bindCommand("infpray", Privilege.ADMIN_POWER) {
    val p = it.player()
    p.toggleVarbit(Prayers.INF_PRAY_VARBIT)
    p.message("Infinite prayer: ${if (p.getVarbit(Prayers.INF_PRAY_VARBIT) == 0) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

/**
 * Activate prayers.
 */
Prayer.values.forEach { prayer ->
    r.bindButton(parent = 541, child = prayer.child) {
        it.suspendable {
            Prayers.toggle(it, prayer)
        }
    }
}

/**
 * Prayer drain.
 */
r.bindLogin {
    it.player().timers[Prayers.PRAYER_DRAIN] = 1
}

r.bindTimer(Prayers.PRAYER_DRAIN) {
    val p = it.player()
    p.timers[Prayers.PRAYER_DRAIN] = 1
    Prayers.drainPrayer(p)
}

/**
 * Toggle quick-prayers.
 */
r.bindButton(parent = 160, child = 14) {
    val p = it.player()
    val opt = it.getInteractingOption()
    Prayers.toggleQuickPrayers(p, opt)
}

/**
 * Select quick-prayer.
 */
r.bindButton(parent = 77, child = 4) {
    val slot = it.getInteractingSlot()
    val prayer = Prayer.values.firstOrNull { prayer -> prayer.quickPrayerSlot == slot } ?: return@bindButton
    Prayers.selectQuickPrayer(it, prayer)
}

/**
 * Accept selected quick-prayer.
 */
r.bindButton(parent = 77, child = 5) {
    val p = it.player()
    p.openInterface(InterfacePane.PRAYER)
}