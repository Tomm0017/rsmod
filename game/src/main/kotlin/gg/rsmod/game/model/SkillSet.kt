package gg.rsmod.game.model

/**
 * Holds all [Skill] data for a player.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SkillSet(val maxSkills: Int) {

    companion object {

        /**
         * The maximum amount of xp that can be set on a skill.
         */
        const val MAX_XP = 200_000_000

        const val MAX_LVL = 99

        /**
         * Gets the level correspondent to the [xp] given.
         */
        fun getLevelForXp(xp: Double): Int {
            for (guess in 1 until XP_TABLE.size) {
                if (xp < XP_TABLE[guess]) {
                    return guess
                }
            }
            return MAX_LVL
        }

        /**
         * Gets the xp you need to achieve to first reach [level].
         */
        fun getXpForLevel(level: Int): Double = XP_TABLE[level - 1].toDouble()

        /**
         * A table of the amount of xp needed to achieve 126 levels in a skill.
         * If RS ever adds over 126 for any revision, we can increase this.
         */
        private val XP_TABLE = IntArray(126).apply {
            var points = 0
            for (level in 1 until size) {
                points += Math.floor(level + 300 * Math.pow(2.0, level / 7.0)).toInt()
                set(level, points / 4)
            }
        }
    }

    private val skills = arrayListOf<Skill>().apply {
        for (i in 0 until maxSkills) {
            add(Skill(id = i))
        }
    }

    /**
     * The current combat level. This must be set externally by a login plugin
     * that is used on whatever revision you want.
     */
    var combatLevel = 3

    /**
     * A flag which indicates if the skill's level and xp need to be sent to
     * the client on the next cycle.
     */
    var dirty = BooleanArray(maxSkills) { true }

    /**
     * Get the [Skill] in [skills] with [skill] as its index
     */
    operator fun get(skill: Int): Skill = skills[skill]

    /**
     * If the [skill] data needs to be sent to the client.
     */
    fun isDirty(skill: Int): Boolean = dirty[skill]

    /**
     * Reset the [dirty] flag on all skills.
     */
    fun clean() {
        for (i in 0 until dirty.size) {
            dirty[i] = false
        }
    }

    fun getCurrentXp(skill: Int): Double = skills[skill].xp

    fun getCurrentLevel(skill: Int): Int = skills[skill].currentLevel

    /**
     * Gets the level based on the xp that [skills].get([skill]) has.
     */
    fun getMaxLevel(skill: Int): Int = getLevelForXp(skills[skill].xp)

    fun setXp(skill: Int, xp: Double) {
        get(skill).xp = xp
        dirty[skill] = true
    }

    /**
     * Sets the 'current'/temporary level of the [skill].
     */
    fun setCurrentLevel(skill: Int, level: Int) {
        get(skill).currentLevel = level
        dirty[skill] = true
    }

    /**
     * Sets the base, or real, level of the skill.
     */
    fun setBaseLevel(skill: Int, level: Int) {
        setBaseXp(skill, getXpForLevel(level))
    }

    /**
     * Sets the base xp of the skill.
     */
    fun setBaseXp(skill: Int, xp: Double) {
        setXp(skill, xp)
        setCurrentLevel(skill, getLevelForXp(xp))
    }

    fun calculateTotalLevel(): Int = skills.sumBy { skill -> getMaxLevel(skill.id) }

    fun calculateTotalXp(): Double = skills.sumByDouble { skill -> getCurrentXp(skill.id) }
}