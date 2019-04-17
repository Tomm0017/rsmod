package gg.rsmod.game.model.skill

/**
 * Holds all [Skill] data for a player.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SkillSet(val maxSkills: Int) {

    private val skills = Array(maxSkills) { index -> Skill(index) }

    /**
     * A flag which indicates if the skill's level and xp need to be sent to
     * the client on the next cycle.
     */
    var dirty = BooleanArray(maxSkills) { true }

    /**
     * Sum up the max level for all your skills.
     */
    val calculateTotalLevel: Int get() = skills.sumBy { skill -> getMaxLevel(skill.id) }

    /**
     * Sum up the experience for all your skills.
     */
    val calculateTotalXp: Double get() = skills.sumByDouble { skill -> getCurrentXp(skill.id) }

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
    fun clean(skill: Int) {
        dirty[skill] = false
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
     * Sets the xp of the skill while also setting the current level
     * to the level respective to [xp].
     */
    fun setBaseXp(skill: Int, xp: Double) {
        setXp(skill, xp)
        setCurrentLevel(skill, getLevelForXp(xp))
    }

    /**
     * Alters the current level of the skill by adding [value] onto it.
     *
     * @param skill
     * The skill level to alter.
     *
     * @param value
     * The value which to add onto the current skill level. This value can be
     * negative.
     *
     * @param capValue
     * The amount of levels which can be surpass the max level in the skill.
     * For example, if this value is set to [3] on a skill that has is [99],
     * that means that the level can be altered from [99] to [102].
     */
    fun alterCurrentLevel(skill: Int, value: Int, capValue: Int = 0) {
        val newLevel = Math.max(0, Math.min(getCurrentLevel(skill) + value, getMaxLevel(skill) + capValue))
        val curLevel = getCurrentLevel(skill)

        if (newLevel != curLevel) {
            setCurrentLevel(skill = skill, level = newLevel)
        }
    }

    /**
     * Restore all skill levels back to normal.
     */
    fun restoreAll() {
        skills.forEach { skill ->
            setCurrentLevel(skill.id, getMaxLevel(skill.id))
        }
    }

    companion object {

        /**
         * The maximum amount of xp that can be set on a skill.
         */
        const val MAX_XP = 200_000_000

        /**
         * The maximum level a skill can reach.
         */
        private const val MAX_LVL = 99

        /**
         * The default amount of trainable skills by players.
         */
        const val DEFAULT_SKILL_COUNT = 23

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
         * A table of the amount of xp needed to achieve 99 levels in a skill.
         */
        private val XP_TABLE = IntArray(99).apply {
            var points = 0
            for (level in 1 until size) {
                points += Math.floor(level + 300 * Math.pow(2.0, level / 7.0)).toInt()
                set(level, points / 4)
            }
        }
    }
}