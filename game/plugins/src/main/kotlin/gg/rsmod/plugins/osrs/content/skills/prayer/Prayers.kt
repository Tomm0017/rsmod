package gg.rsmod.plugins.osrs.content.skills.prayer

import gg.rsmod.game.model.AttributeKey
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.sync.UpdateBlockType
import gg.rsmod.plugins.*
import gg.rsmod.plugins.osrs.GameframeTab
import gg.rsmod.plugins.osrs.InterfacePane
import gg.rsmod.plugins.osrs.api.BonusSlot
import gg.rsmod.plugins.osrs.api.Skills

object Prayers {

    private val PRAYER_DRAIN_COUNTER = AttributeKey<Int>()

    val PRAYER_DRAIN = TimerKey()
    private val DISABLE_OVERHEADS = TimerKey()

    private const val DEACTIVATE_PRAYER_SOUND = 2663

    private const val ACTIVE_PRAYERS_VARP = 83
    private const val SELECTED_QUICK_PRAYERS_VARP = 84

    const val INF_PRAY_VARBIT = 5314
    private const val QUICK_PRAYERS_ACTIVE_VARBIT = 4103
    private const val KING_RANSOMS_QUEST_VARBIT = 3909 // Used for chivalry/piety prayer.
    private const val RIGOUR_UNLOCK_VARBIT = 5451
    private const val AUGURY_UNLOCK_VARBIT = 5452
    private const val PRESERVE_UNLOCK_VARBIT = 5453

    fun disableOverheads(p: Player, cycles: Int) {
        p.timers[DISABLE_OVERHEADS] = cycles
    }

    fun deactivateAll(p: Player) {
        p.setVarp(ACTIVE_PRAYERS_VARP, 0)
        p.setVarbit(QUICK_PRAYERS_ACTIVE_VARBIT, 0)

        if (p.prayerIcon != -1) {
            p.prayerIcon = -1
            p.addBlock(UpdateBlockType.APPEARANCE)
        }
    }

    suspend fun toggle(it: Plugin, prayer: Prayer) {
        val p = it.player()

        if (p.isDead() || !p.lock.canUsePrayer()) {
            p.syncVarp(ACTIVE_PRAYERS_VARP)
            return
        } else if (!checkRequirements(it, prayer)) {
            return
        } else if (prayer.group == PrayerGroup.OVERHEAD && p.timers.has(DISABLE_OVERHEADS)) {
            p.syncVarp(ACTIVE_PRAYERS_VARP)
            p.message("You cannot use overhead prayers right now.")
            return
        } else if (p.getSkills().getCurrentLevel(Skills.PRAYER) == 0) {
            return
        }

        it.interruptAction = { p.syncVarp(ACTIVE_PRAYERS_VARP) }
        while (p.lock.delaysPrayer()) {
            it.wait(1)
        }
        val active = p.getVarbit(prayer.varbit) != 0
        if (active) {
            deactivate(p, prayer)
        } else {
            activate(p, prayer)
        }
        p.setVarbit(QUICK_PRAYERS_ACTIVE_VARBIT, 0)
    }

    fun activate(p: Player, prayer: Prayer) {
        if (!isActive(p, prayer)) {
            val others = Prayer.values.filter { other -> prayer != other && other.group != null &&
                    (prayer.group == other.group || prayer.overlap.contains(other.group)) }
            others.forEach { other ->
                if (p.getVarbit(other.varbit) != 0) {
                    p.setVarbit(other.varbit, 0)
                }
            }

            p.setVarbit(prayer.varbit, 1)
            if (prayer.sound != -1) {
                p.playSound(prayer.sound)
            }

            setOverhead(p)
        }
    }

    fun deactivate(p: Player, prayer: Prayer) {
        if (isActive(p, prayer)) {
            p.setVarbit(prayer.varbit, 0)
            p.playSound(DEACTIVATE_PRAYER_SOUND)
            setOverhead(p)
        }
    }

    fun drainPrayer(p: Player) {
        if (p.isDead() || p.getVarp(Prayers.ACTIVE_PRAYERS_VARP) == 0 || p.getVarbit(Prayers.INF_PRAY_VARBIT) == 1) {
            p.attr.remove(Prayers.PRAYER_DRAIN_COUNTER)
            return
        }

        val drain = Prayers.calculateDrainRate(p)
        if (drain > 0) {
            val counter = p.attr.getOrDefault(Prayers.PRAYER_DRAIN_COUNTER, 0) + drain
            val resistance = 60 + (p.getBonus(BonusSlot.PRAYER) * 2)
            if (counter >= resistance) {
                val points = Math.floor((counter / resistance).toDouble()).toInt()
                p.getSkills().alterCurrentLevel(Skills.PRAYER, -points)
                p.attr.put(Prayers.PRAYER_DRAIN_COUNTER, counter - (resistance * points))
            } else {
                p.attr.put(Prayers.PRAYER_DRAIN_COUNTER, counter)
            }
        }

        if (p.getSkills().getCurrentLevel(Skills.PRAYER) == 0) {
            Prayers.deactivateAll(p)
            p.message("You have run out of prayer points, you must recharge at an altar.")
        }
    }

    fun selectQuickPrayer(it: Plugin, prayer: Prayer) {
        val p = it.player()

        if (p.isDead() || !p.lock.canUsePrayer()) {
            it.player().setVarbit(Prayers.QUICK_PRAYERS_ACTIVE_VARBIT, 0)
            return
        }

        val slot = prayer.quickPrayerSlot
        val enabled = (it.player().getVarp(Prayers.SELECTED_QUICK_PRAYERS_VARP) and (1 shl slot)) != 0

        it.suspendable {
            if (!enabled) {
                if (Prayers.checkRequirements(it, prayer)) {
                    val others = Prayer.values.filter { other -> prayer != other && other.group != null &&
                            (prayer.group == other.group || prayer.overlap.contains(other.group)) }
                    others.forEach { other ->
                        val otherEnabled = (it.player().getVarp(Prayers.SELECTED_QUICK_PRAYERS_VARP) and (1 shl other.quickPrayerSlot)) != 0
                        if (otherEnabled) {
                            p.setVarp(Prayers.SELECTED_QUICK_PRAYERS_VARP, it.player().getVarp(Prayers.SELECTED_QUICK_PRAYERS_VARP) and (1 shl other.quickPrayerSlot).inv())
                        }
                    }
                    p.setVarp(Prayers.SELECTED_QUICK_PRAYERS_VARP, it.player().getVarp(Prayers.SELECTED_QUICK_PRAYERS_VARP) or (1 shl slot))
                }
            } else {
                p.setVarp(Prayers.SELECTED_QUICK_PRAYERS_VARP, it.player().getVarp(Prayers.SELECTED_QUICK_PRAYERS_VARP) and (1 shl slot).inv())
            }
        }
    }

    fun toggleQuickPrayers(p: Player, opt: Int) {
        if (p.isDead() || !p.lock.canUsePrayer()) {
            p.setVarbit(Prayers.QUICK_PRAYERS_ACTIVE_VARBIT, 0)
            return
        }

        if (opt == 0) {
            val quickPrayers = p.getVarp(Prayers.SELECTED_QUICK_PRAYERS_VARP)

            if (quickPrayers == 0) {
                p.setVarbit(Prayers.QUICK_PRAYERS_ACTIVE_VARBIT, 0)
                p.message("You haven't selected any quick-prayers.")
            } else if (p.getSkills().getCurrentLevel(Skills.PRAYER) <= 0) {
                p.setVarbit(Prayers.QUICK_PRAYERS_ACTIVE_VARBIT, 0)
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

    private fun isActive(p: Player, prayer: Prayer): Boolean = p.getVarbit(prayer.varbit) != 0

    private suspend fun checkRequirements(it: Plugin, prayer: Prayer): Boolean {
        val p = it.player()

        if (p.getVarbit(INF_PRAY_VARBIT) == 1) {
            return true
        }

        if (p.getSkills().getMaxLevel(Skills.PRAYER) < prayer.level) {
            p.syncVarp(ACTIVE_PRAYERS_VARP)
            it.messageDialog("You need a <col=000080>Prayer</col> level of ${prayer.level} to use <col=000080>${prayer.named}.")
            return false
        }

        // TODO(Tom): get correct messages for these unlockable
        if (prayer == Prayer.PRESERVE && p.getVarbit(PRESERVE_UNLOCK_VARBIT) == 0) {
            p.syncVarp(ACTIVE_PRAYERS_VARP)
            it.messageDialog("You have not unlocked this prayer.")
            return false
        }

        if (prayer == Prayer.CHIVALRY && p.getVarbit(KING_RANSOMS_QUEST_VARBIT) < 8) {
            p.syncVarp(ACTIVE_PRAYERS_VARP)
            it.messageDialog("You have not unlocked this prayer.")
            return false
        }

        if (prayer == Prayer.PIETY && p.getVarbit(KING_RANSOMS_QUEST_VARBIT) < 8) {
            p.syncVarp(ACTIVE_PRAYERS_VARP)
            it.messageDialog("You have not unlocked this prayer.")
            return false
        }

        if (prayer == Prayer.RIGOUR && p.getVarbit(RIGOUR_UNLOCK_VARBIT) == 0) {
            p.syncVarp(ACTIVE_PRAYERS_VARP)
            it.messageDialog("You have not unlocked this prayer.")
            return false
        }

        if (prayer == Prayer.AUGURY && p.getVarbit(AUGURY_UNLOCK_VARBIT) == 0) {
            p.syncVarp(ACTIVE_PRAYERS_VARP)
            it.messageDialog("You have not unlocked this prayer.")
            return false
        }

        return true
    }

    private fun setOverhead(p: Player) {
        val icon = when {
            isActive(p, Prayer.PROTECT_FROM_MELEE) -> 0
            isActive(p, Prayer.PROTECT_FROM_MISSILES) -> 1
            isActive(p, Prayer.PROTECT_FROM_MAGIC) -> 2
            isActive(p, Prayer.RETRIBUTION) -> 3
            isActive(p, Prayer.SMITE) -> 4
            isActive(p, Prayer.REDEMPTION) -> 5
            else -> -1
        }

        if (p.prayerIcon != icon) {
            p.prayerIcon = icon
            p.addBlock(UpdateBlockType.APPEARANCE)
        }
    }

    private fun calculateDrainRate(p: Player): Int = Prayer.values.filter { isActive(p, it) }.sumBy { it.drainEffect }
}