package gg.rsmod.plugins.content.mechanics.prayer

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class Prayer(val named: String, val child: Int, val quickPrayerSlot: Int, val varbit: Int,
                  val level: Int, val sound: Int, val drainEffect: Int, val group: PrayerGroup?,
                  vararg val overlap: PrayerGroup) {

    THICK_SKIN(named = "Thick Skin", child = 5, quickPrayerSlot = 0, varbit = 4104, level = 1, sound = 2690, drainEffect = 3,
            group = PrayerGroup.DEFENCE, overlap = *arrayOf(PrayerGroup.COMBAT)),

    BURST_OF_STRENGTH(named = "Burst of Strength", child = 6, quickPrayerSlot = 1, varbit = 4105, level = 4, sound = 2688, drainEffect = 3,
            group = PrayerGroup.STRENGTH, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

    CLARITY_OF_THOUGHT(named = "Clarity of Thought", child = 7, quickPrayerSlot = 2, varbit = 4106, level = 7, sound = 2664, drainEffect = 3,
            group = PrayerGroup.ATTACK, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

    SHARP_EYE(named = "Sharp Eye", child = 23, quickPrayerSlot = 18, varbit = 4122, level = 8, sound = 2685, drainEffect = 3,
            group = PrayerGroup.RANGED, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

    MYSTIC_WILL(named = "Mystic Will", child = 24, quickPrayerSlot = 19, varbit = 4123, level = 9, sound = 2670, drainEffect = 3,
            group = PrayerGroup.MAGIC, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.RANGED, PrayerGroup.COMBAT)),

    ROCK_SKIN(named = "Rock Skin", child = 8, quickPrayerSlot = 3, varbit = 4107, level = 10, sound = 2684, drainEffect = 6,
            group = PrayerGroup.DEFENCE, overlap = *arrayOf(PrayerGroup.COMBAT)),

    SUPERHUMAN_STRENGTH(named = "Superhuman Strength", child = 9, quickPrayerSlot = 4, varbit = 4108, level = 13, sound = 2689, drainEffect = 6,
            group = PrayerGroup.STRENGTH, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

    IMPROVED_REFLEXES(named = "Improved Reflexes", child = 10, quickPrayerSlot = 5, varbit = 4109, level = 16, sound = 2662, drainEffect = 6,
            group = PrayerGroup.ATTACK, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

    RAPID_RESTORE(named = "Rapid Restore", child = 11, quickPrayerSlot = 6, varbit = 4110, level = 19, sound = 2679, drainEffect = 1,
            group = null, overlap = *arrayOf()),

    RAPID_HEAL(named = "Rapid Heal", child = 12, quickPrayerSlot = 7, varbit = 4111, level = 22, sound = 2678, drainEffect = 2,
            group = null, overlap = *arrayOf()),

    PROTECT_ITEM(named = "Protect Item", child = 13, quickPrayerSlot = 8, varbit = 4112, level = 25, sound = 1982, drainEffect = 2,
            group = null, overlap = *arrayOf()),

    HAWK_EYE(named = "Hawk Eye", child = 25, quickPrayerSlot = 20, varbit = 4124, level = 26, sound = 2666, drainEffect = 6,
            group = PrayerGroup.RANGED, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

    MYSTIC_LORE(named = "Mystic Lore", child = 26, quickPrayerSlot = 21, varbit = 4125, level = 27, sound = 2668, drainEffect = 6,
            group = PrayerGroup.MAGIC, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.RANGED, PrayerGroup.COMBAT)),

    STEEL_SKIN(named = "Steel Skin", child = 14, quickPrayerSlot = 9, varbit = 4113, level = 28, sound = 2687, drainEffect = 12,
            group = PrayerGroup.DEFENCE, overlap = *arrayOf(PrayerGroup.COMBAT)),

    ULTIMATE_STRENGTH(named = "Ultimate Strength", child = 15, quickPrayerSlot = 10, varbit = 4114, level = 31, sound = 2691, drainEffect = 12,
            group = PrayerGroup.STRENGTH, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

    INCREDIBLE_REFLEXES(named = "Incredible Reflexes", child = 16, quickPrayerSlot = 11, varbit = 4115, level = 34, sound = 2667, drainEffect = 12,
            group = PrayerGroup.ATTACK, overlap = *arrayOf(PrayerGroup.RANGED, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

    PROTECT_FROM_MAGIC(named = "Protect from Magic", child = 17, quickPrayerSlot = 12, varbit = 4116, level = 37, sound = 2675, drainEffect = 12,
            group = PrayerGroup.OVERHEAD_PROTECTION, overlap = *arrayOf()),

    PROTECT_FROM_MISSILES(named = "Protect from Missiles", child = 18, quickPrayerSlot = 13, varbit = 4117, level = 40, sound = 2677, drainEffect = 12,
            group = PrayerGroup.OVERHEAD_PROTECTION, overlap = *arrayOf()),

    PROTECT_FROM_MELEE(named = "Protect from Melee", child = 19, quickPrayerSlot = 14, varbit = 4118, level = 43, sound = 2676, drainEffect = 12,
            group = PrayerGroup.OVERHEAD_PROTECTION, overlap = *arrayOf()),

    EAGLE_EYE(named = "Eagle Eye", child = 27, quickPrayerSlot = 22, varbit = 4126, level = 44, sound = 2665, drainEffect = 12,
            group = PrayerGroup.RANGED, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.MAGIC, PrayerGroup.COMBAT)),

    MYSTIC_MIGHT(named = "Mystic Might", child = 28, quickPrayerSlot = 23, varbit = 4127, level = 45, sound = 2669, drainEffect = 12,
            group = PrayerGroup.MAGIC, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.RANGED, PrayerGroup.COMBAT)),

    RETRIBUTION(named = "Retribution", child = 20, quickPrayerSlot = 15, varbit = 4119, level = 46, sound = 2682, drainEffect = 3,
            group = PrayerGroup.OVERHEAD_PRAYER, overlap = *arrayOf()),

    REDEMPTION(named = "Redemption", child = 21, quickPrayerSlot = 16, varbit = 4120, level = 49, sound = 2680, drainEffect = 6,
            group = PrayerGroup.OVERHEAD_PRAYER, overlap = *arrayOf()),

    SMITE(named = "Smite", child = 22, quickPrayerSlot = 17, varbit = 4121, level = 52, sound = 2686, drainEffect = 18,
            group = PrayerGroup.OVERHEAD_PRAYER, overlap = *arrayOf()),

    PRESERVE(named = "Preserve", child = 33, quickPrayerSlot = 28, varbit = 5466, level = 55, sound = 3825, drainEffect = 3,
            group = null, overlap = *arrayOf()),

    CHIVALRY(named = "Chivalry", child = 29, quickPrayerSlot = 25, varbit = 4128, level = 60, sound = 3826, drainEffect = 24,
            group = PrayerGroup.COMBAT, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.DEFENCE, PrayerGroup.RANGED, PrayerGroup.MAGIC)),

    PIETY(named = "Piety", child = 30, quickPrayerSlot = 26, varbit = 4129, level = 70, sound = 3825, drainEffect = 24,
            group = PrayerGroup.COMBAT, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.DEFENCE, PrayerGroup.RANGED, PrayerGroup.MAGIC)),

    RIGOUR(named = "Rigour", child = 31, quickPrayerSlot = 24, varbit = 5464, level = 74, sound = 3825, drainEffect = 24,
            group = PrayerGroup.COMBAT, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.DEFENCE, PrayerGroup.RANGED, PrayerGroup.MAGIC)),

    AUGURY(named = "Augury", child = 32, quickPrayerSlot = 27, varbit = 5465, level = 77, sound = 3825, drainEffect = 24,
            group = PrayerGroup.COMBAT, overlap = *arrayOf(PrayerGroup.ATTACK, PrayerGroup.STRENGTH, PrayerGroup.DEFENCE, PrayerGroup.RANGED, PrayerGroup.MAGIC)),
    ;

    companion object {
        val values = enumValues<Prayer>()
    }
}