package gg.rsmod.plugins.osrs.content.skills.prayer

import gg.rsmod.game.model.AttributeKey
import gg.rsmod.game.model.Privilege
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.game.sync.UpdateBlockType
import gg.rsmod.plugins.*
import gg.rsmod.plugins.osrs.model.BonusSlot
import gg.rsmod.plugins.osrs.GameframeTab
import gg.rsmod.plugins.osrs.InterfacePane
import gg.rsmod.plugins.osrs.model.Skills

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Prayers {

    private val PRAYER_DRAIN_COUNTER = AttributeKey<Int>()

    private val PRAYER_DRAIN = TimerKey()
    private val DISABLE_OVERHEADS = TimerKey()

    private const val DEACTIVATE_PRAYER_SOUND = 2663

    private const val ACTIVE_PRAYERS_VARP = 83
    private const val SELECTED_QUICK_PRAYERS_VARP = 84

    private const val INF_PRAY_VARBIT = 5314
    private const val QUICK_PRAYERS_ACTIVE_VARBIT = 4103
    private const val KING_RANSOMS_QUEST_VARBIT = 3909 // Used for chivalry/piety prayer.
    private const val RIGOUR_UNLOCK_VARBIT = 5451
    private const val AUGURY_UNLOCK_VARBIT = 5452
    private const val PRESERVE_UNLOCK_VARBIT = 5453

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        /**
         * Infinite prayer command.
         */
        r.bindCommand("infpray", Privilege.ADMIN_POWER) {
            val p = it.player()
            p.toggleVarbit(INF_PRAY_VARBIT)
            p.message("Infinite prayer: ${if (p.getVarbit(Prayers.INF_PRAY_VARBIT) == 0) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
        }

        /**
         * Activate prayers.
         */
        Prayer.values.forEach { prayer ->
            r.bindButton(parent = 541, child = prayer.child) {
                it.suspendable {
                    toggle(it, prayer)
                }
            }
        }

        /**
         * Prayer drain.
         */
        r.bindLogin {
            it.player().timers[PRAYER_DRAIN] = 1
        }

        r.bindTimer(PRAYER_DRAIN) {
            val p = it.player()

            p.timers[PRAYER_DRAIN] = 1

            if (p.isDead() || p.getVarp(ACTIVE_PRAYERS_VARP) == 0 || p.getVarbit(INF_PRAY_VARBIT) == 1) {
                p.attr.remove(PRAYER_DRAIN_COUNTER)
                return@bindTimer
            }

            val drain = calculateDrainRate(p)
            if (drain > 0) {
                val counter = p.attr.getOrDefault(PRAYER_DRAIN_COUNTER, 0) + drain
                val resistance = 60 + (p.getBonus(BonusSlot.PRAYER) * 2)
                if (counter >= resistance) {
                    val points = Math.floor((counter / resistance).toDouble()).toInt()
                    p.getSkills().alterCurrentLevel(Skills.PRAYER, -points)
                    p.attr.put(PRAYER_DRAIN_COUNTER, counter - (resistance * points))
                } else {
                    p.attr.put(PRAYER_DRAIN_COUNTER, counter)
                }
            }

            if (p.getSkills().getCurrentLevel(Skills.PRAYER) == 0) {
                deactivateAll(p)
                p.message("You have run out of prayer points, you must recharge at an altar.")
            }
        }

        /**
         * Toggle quick-prayers.
         */
        r.bindButton(parent = 160, child = 14) {
            val p = it.player()
            val opt = it.getInteractingOption()

            if (p.isDead() || !p.lock.canUsePrayer()) {
                it.player().setVarbit(QUICK_PRAYERS_ACTIVE_VARBIT, 0)
                return@bindButton
            }

            if (opt == 0) {
                val quickPrayers = p.getVarp(SELECTED_QUICK_PRAYERS_VARP)

                if (quickPrayers == 0) {
                    it.player().setVarbit(QUICK_PRAYERS_ACTIVE_VARBIT, 0)
                    p.message("You haven't selected any quick-prayers.")
                } else if (p.getSkills().getCurrentLevel(Skills.PRAYER) <= 0) {
                    it.player().setVarbit(QUICK_PRAYERS_ACTIVE_VARBIT, 0)
                    p.message("You have run out of prayer points, you must recharge at an altar.")
                } else if (p.getVarp(ACTIVE_PRAYERS_VARP) == quickPrayers) {
                    /**
                     * All active prayers are quick-prayers - so we turn them off.
                     */
                    p.setVarp(ACTIVE_PRAYERS_VARP, 0)
                    p.setVarbit(QUICK_PRAYERS_ACTIVE_VARBIT, 0)
                    setOverhead(p)
                } else {
                    p.setVarp(ACTIVE_PRAYERS_VARP, quickPrayers)
                    p.setVarbit(QUICK_PRAYERS_ACTIVE_VARBIT, 1)
                    setOverhead(p)
                }
            } else if (opt == 1) {
                p.setInterfaceSetting(parent = 77, child = 4, from = 0, to = 29, setting = 2)
                p.openInterface(interfaceId = 77, pane = InterfacePane.PRAYER)
                p.openTab(GameframeTab.PRAYER)
            }
        }

        /**
         * Select quick-prayers.
         */
        r.bindButton(parent = 77, child = 4) {
            val p = it.player()
            val slot = it.getInteractingSlot()
            val prayer = Prayer.values.firstOrNull { prayer -> prayer.quickPrayerSlot == slot } ?: return@bindButton

            if (p.isDead() || !p.lock.canUsePrayer()) {
                it.player().setVarbit(QUICK_PRAYERS_ACTIVE_VARBIT, 0)
                return@bindButton
            }

            val enabled = (it.player().getVarp(SELECTED_QUICK_PRAYERS_VARP) and (1 shl slot)) != 0

            it.suspendable {
                if (!enabled) {
                    if (checkRequirements(it, prayer)) {
                        val others = Prayer.values.filter { other -> prayer != other && other.group != null &&
                                (prayer.group == other.group || prayer.overlap.contains(other.group)) }
                        others.forEach { other ->
                            val otherEnabled = (it.player().getVarp(SELECTED_QUICK_PRAYERS_VARP) and (1 shl other.quickPrayerSlot)) != 0
                            if (otherEnabled) {
                                p.setVarp(SELECTED_QUICK_PRAYERS_VARP, it.player().getVarp(SELECTED_QUICK_PRAYERS_VARP) and (1 shl other.quickPrayerSlot).inv())
                            }
                        }
                        p.setVarp(SELECTED_QUICK_PRAYERS_VARP, it.player().getVarp(SELECTED_QUICK_PRAYERS_VARP) or (1 shl slot))
                    }
                } else {
                    p.setVarp(SELECTED_QUICK_PRAYERS_VARP, it.player().getVarp(SELECTED_QUICK_PRAYERS_VARP) and (1 shl slot).inv())
                }
            }
        }

        r.bindButton(parent = 77, child = 5) {
            val p = it.player()
            p.openInterface(InterfacePane.PRAYER)
        }
    }

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

    private suspend fun toggle(it: Plugin, prayer: Prayer) {
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

    fun isActive(p: Player, prayer: Prayer): Boolean = p.getVarbit(prayer.varbit) != 0

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

        // TODO(Tom): get correct messages for these unlockable prayers.
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

    enum class Prayer(val named: String, val child: Int, val quickPrayerSlot: Int, val varbit: Int,
                      val level: Int, val sound: Int, val drainEffect: Int, val group: PrayerGroup?,
                      vararg val overlap: PrayerGroup) {

        THICK_SKIN(named = "Thick Skin", child = 4, quickPrayerSlot = 0, varbit = 4104, level = 1, sound = 2690, drainEffect = 3,
                group = PrayerGroup.DEFENCE, overlap = *arrayOf(PrayerGroup.COMBAT)),

        BURST_OF_STRENGTH(named = "Burst of Strength", child = 5, quickPrayerSlot = 1, varbit = 4105, level = 4, sound = 2688, drainEffect = 3,
                group = PrayerGroup.STRENGTH, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

        CLARITY_OF_THOUGHT(named = "Clarity of Thought", child = 6, quickPrayerSlot = 2, varbit = 4106, level = 7, sound = 2664, drainEffect = 3,
                group = PrayerGroup.ATTACK, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

        SHARP_EYE(named = "Sharp Eye", child = 22, quickPrayerSlot = 18, varbit = 4122, level = 8, sound = 2685, drainEffect = 3,
                group = PrayerGroup.RANGED, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

        MYSTIC_WILL(named = "Mystic Will", child = 23, quickPrayerSlot = 19, varbit = 4123, level = 9, sound = 2670, drainEffect = 3,
                group = PrayerGroup.MAGIC, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.RANGED, PrayerGroup.COMBAT)),

        ROCK_SKIN(named = "Rock Skin", child = 7, quickPrayerSlot = 3, varbit = 4107, level = 10, sound = 2684, drainEffect = 6,
                group = PrayerGroup.DEFENCE, overlap = *arrayOf(PrayerGroup.COMBAT)),

        SUPERHUMAN_STRENGTH(named = "Superhuman Strength", child = 8, quickPrayerSlot = 4, varbit = 4108, level = 13, sound = 2689, drainEffect = 6,
                group = PrayerGroup.STRENGTH, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

        IMPROVED_REFLEXES(named = "Improved Reflexes", child = 9, quickPrayerSlot = 5, varbit = 4109, level = 16, sound = 2662, drainEffect = 6,
                group = PrayerGroup.ATTACK, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

        RAPID_RESTORE(named = "Rapid Restore", child = 10, quickPrayerSlot = 6, varbit = 4110, level = 19, sound = 2679, drainEffect = 1,
                group = null, overlap = *arrayOf()),

        RAPID_HEAL(named = "Rapid Heal", child = 11, quickPrayerSlot = 7, varbit = 4111, level = 22, sound = 2678, drainEffect = 2,
                group = null, overlap = *arrayOf()),

        PROTECT_ITEM(named = "Protect Item", child = 12, quickPrayerSlot = 8, varbit = 4112, level = 25, sound = 1982, drainEffect = 2,
                group = null, overlap = *arrayOf()),

        HAWK_EYE(named = "Hawk Eye", child = 24, quickPrayerSlot = 20, varbit = 4124, level = 26, sound = 2666, drainEffect = 6,
                group = PrayerGroup.RANGED, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

        MYSTIC_LORE(named = "Mystic Lore", child = 25, quickPrayerSlot = 21, varbit = 4125, level = 27, sound = 2668, drainEffect = 6,
                group = PrayerGroup.MAGIC, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.RANGED, PrayerGroup.COMBAT)),

        STEEL_SKIN(named = "Steel Skin", child = 13, quickPrayerSlot = 9, varbit = 4113, level = 28, sound = 2687, drainEffect = 12,
                group = PrayerGroup.DEFENCE, overlap = *arrayOf(PrayerGroup.COMBAT)),

        ULTIMATE_STRENGTH(named = "Ultimate Strength", child = 14, quickPrayerSlot = 10, varbit = 4114, level = 31, sound = 2691, drainEffect = 12,
                group = PrayerGroup.STRENGTH, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

        INCREDIBLE_REFLEXES(named = "Incredible Reflexes", child = 15, quickPrayerSlot = 11, varbit = 4115, level = 34, sound = 2667, drainEffect = 12,
                group = PrayerGroup.ATTACK, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

        PROTECT_FROM_MAGIC(named = "Protect from Magic", child = 16, quickPrayerSlot = 12, varbit = 4116, level = 37, sound = 2675, drainEffect = 12,
                group = PrayerGroup.OVERHEAD, overlap = *arrayOf()),

        PROTECT_FROM_MISSILES(named = "Protect from Missiles", child = 17, quickPrayerSlot = 13, varbit = 4117, level = 40, sound = 2677, drainEffect = 12,
                group = PrayerGroup.OVERHEAD, overlap = *arrayOf()),

        PROTECT_FROM_MELEE(named = "Protect from Melee", child = 18, quickPrayerSlot = 14, varbit = 4118, level = 43, sound = 2676, drainEffect = 12,
                group = PrayerGroup.OVERHEAD, overlap = *arrayOf()),

        EAGLE_EYE(named = "Eagle Eye", child = 26, quickPrayerSlot = 22, varbit = 4126, level = 44, sound = 2665, drainEffect = 12,
                group = PrayerGroup.RANGED, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

        MYSTIC_MIGHT(named = "Mystic Might", child = 27, quickPrayerSlot = 23, varbit = 4127, level = 45, sound = 2669, drainEffect = 12,
                group = PrayerGroup.MAGIC, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.RANGED, PrayerGroup.COMBAT)),

        RETRIBUTION(named = "Retribution", child = 19, quickPrayerSlot = 15, varbit = 4119, level = 46, sound = 2682, drainEffect = 3,
                group = PrayerGroup.OVERHEAD, overlap = *arrayOf()),

        REDEMPTION(named = "Redemption", child = 20, quickPrayerSlot = 16, varbit = 4120, level = 49, sound = 2680, drainEffect = 6,
                group = PrayerGroup.OVERHEAD, overlap = *arrayOf()),

        SMITE(named = "Smite", child = 21, quickPrayerSlot = 17, varbit = 4121, level = 52, sound = 2686, drainEffect = 18,
                group = PrayerGroup.OVERHEAD, overlap = *arrayOf()),

        PRESERVE(named = "Preserve", child = 32, quickPrayerSlot = 28, varbit = 5466, level = 55, sound = 3825, drainEffect = 3,
                group = null, overlap = *arrayOf()),

        CHIVALRY(named = "Chivalry", child = 28, quickPrayerSlot = 25, varbit = 4128, level = 60, sound = 3826, drainEffect = 24,
                group = PrayerGroup.COMBAT, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.DEFENCE, PrayerGroup.RANGED, PrayerGroup.MAGIC)),

        PIETY(named = "Piety", child = 29, quickPrayerSlot = 26, varbit = 4129, level = 70, sound = 3825, drainEffect = 24,
                group = PrayerGroup.COMBAT, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.DEFENCE, PrayerGroup.RANGED, PrayerGroup.MAGIC)),

        RIGOUR(named = "Rigour", child = 30, quickPrayerSlot = 24, varbit = 5464, level = 74, sound = 3825, drainEffect = 24,
                group = PrayerGroup.COMBAT, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.DEFENCE, PrayerGroup.RANGED, PrayerGroup.MAGIC)),

        AUGURY(named = "Augury", child = 31, quickPrayerSlot = 27, varbit = 5465, level = 77, sound = 3825, drainEffect = 24,
                group = PrayerGroup.COMBAT, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.DEFENCE, PrayerGroup.RANGED, PrayerGroup.MAGIC)),
        ;

        companion object {
            val values = enumValues<Prayer>()
        }
    }

    private enum class PrayerGroup {
        OVERHEAD,
        DEFENCE,
        STRENGTH,
        ATTACK,
        RANGED,
        MAGIC,
        COMBAT
    }
}