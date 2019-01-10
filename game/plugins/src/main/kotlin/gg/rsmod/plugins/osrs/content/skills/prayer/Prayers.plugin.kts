
import gg.rsmod.game.model.Privilege
import gg.rsmod.plugins.*
import gg.rsmod.plugins.osrs.GameframeTab
import gg.rsmod.plugins.osrs.InterfacePane
import gg.rsmod.plugins.osrs.api.Skills
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
 * Toggle quick-
 */
r.bindButton(parent = 160, child = 14) {
    val p = it.player()
    val opt = it.getInteractingOption()

    if (p.isDead() || !p.lock.canUsePrayer()) {
        it.player().setVarbit(Prayers.QUICK_PRAYERS_ACTIVE_VARBIT, 0)
        return@bindButton
    }

    if (opt == 0) {
        val quickPrayers = p.getVarp(Prayers.SELECTED_QUICK_PRAYERS_VARP)

        if (quickPrayers == 0) {
            it.player().setVarbit(Prayers.QUICK_PRAYERS_ACTIVE_VARBIT, 0)
            p.message("You haven't selected any quick-")
        } else if (p.getSkills().getCurrentLevel(Skills.PRAYER) <= 0) {
            it.player().setVarbit(Prayers.QUICK_PRAYERS_ACTIVE_VARBIT, 0)
            p.message("You have run out of prayer points, you must recharge at an altar.")
        } else if (p.getVarp(Prayers.ACTIVE_PRAYERS_VARP) == quickPrayers) {
            /**
             * All active prayers are quick-prayers - so we turn them off.
             */
            p.setVarp(Prayers.ACTIVE_PRAYERS_VARP, 0)
            p.setVarbit(Prayers.QUICK_PRAYERS_ACTIVE_VARBIT, 0)
            Prayers.setOverhead(p)
        } else {
            p.setVarp(Prayers.ACTIVE_PRAYERS_VARP, quickPrayers)
            p.setVarbit(Prayers.QUICK_PRAYERS_ACTIVE_VARBIT, 1)
            Prayers.setOverhead(p)
        }
    } else if (opt == 1) {
        p.setInterfaceSetting(parent = 77, child = 4, from = 0, to = 29, setting = 2)
        p.openInterface(interfaceId = 77, pane = InterfacePane.PRAYER)
        p.openTab(GameframeTab.PRAYER)
    }
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