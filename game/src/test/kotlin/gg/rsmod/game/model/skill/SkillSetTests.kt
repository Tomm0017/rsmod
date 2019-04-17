package gg.rsmod.game.model.skill

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SkillSetTests {

    private fun createSkills(): SkillSet = SkillSet(SKILL_COUNT)

    @Test
    fun `set xp for skills`() {
        val skills = createSkills()

        val xp = 6_300_000.0
        skills.setXp(ATTACK_SKILL, xp)
        assertEquals(xp, skills.getCurrentXp(ATTACK_SKILL))
        assertEquals(xp, skills.calculateTotalXp)

        val lvl = SkillSet.getLevelForXp(xp)
        assertEquals(lvl, skills.getMaxLevel(ATTACK_SKILL))
    }

    @Test
    fun `alter levels (capped)`() {
        val skills = createSkills()

        val baseLevel = 50
        skills.setBaseLevel(ATTACK_SKILL, baseLevel)
        assertEquals(skills.getCurrentLevel(ATTACK_SKILL), baseLevel)
        assertEquals(skills.getMaxLevel(ATTACK_SKILL), baseLevel)

        val decrement = 3
        skills.decrementCurrentLevel(ATTACK_SKILL, decrement, capped = true)
        assertEquals(baseLevel - decrement, skills.getCurrentLevel(ATTACK_SKILL))
        assertEquals(baseLevel, skills.getMaxLevel(ATTACK_SKILL))

        skills.decrementCurrentLevel(ATTACK_SKILL, decrement, capped = true)
        assertEquals(baseLevel - decrement, skills.getCurrentLevel(ATTACK_SKILL))

        skills.restore(ATTACK_SKILL)
        assertEquals(baseLevel, skills.getCurrentLevel(ATTACK_SKILL))

        val increment = 3
        skills.incrementCurrentLevel(ATTACK_SKILL, increment, capped = true)
        assertEquals(baseLevel + increment, skills.getCurrentLevel(ATTACK_SKILL))
        assertEquals(baseLevel, skills.getMaxLevel(ATTACK_SKILL))

        skills.incrementCurrentLevel(ATTACK_SKILL, increment, capped = true)
        assertEquals(baseLevel + increment, skills.getCurrentLevel(ATTACK_SKILL))
    }

    @Test
    fun `alter levels (uncapped)`() {
        val skills = createSkills()

        val baseLevel = 50
        skills.setBaseLevel(ATTACK_SKILL, baseLevel)
        assertEquals(skills.getCurrentLevel(ATTACK_SKILL), baseLevel)
        assertEquals(skills.getMaxLevel(ATTACK_SKILL), baseLevel)

        val decrement = 3
        skills.decrementCurrentLevel(ATTACK_SKILL, decrement, capped = false)
        assertEquals(skills.getCurrentLevel(ATTACK_SKILL), baseLevel - decrement)
        assertEquals(skills.getMaxLevel(ATTACK_SKILL), baseLevel)

        skills.decrementCurrentLevel(ATTACK_SKILL, decrement, capped = false)
        assertEquals(skills.getCurrentLevel(ATTACK_SKILL), baseLevel - (decrement * 2))

        skills.restore(ATTACK_SKILL)
        assertEquals(skills.getCurrentLevel(ATTACK_SKILL), baseLevel)

        val increment = 3
        skills.incrementCurrentLevel(ATTACK_SKILL, increment, capped = false)
        assertEquals(skills.getCurrentLevel(ATTACK_SKILL), baseLevel + increment)
        assertEquals(skills.getMaxLevel(ATTACK_SKILL), baseLevel)

        skills.incrementCurrentLevel(ATTACK_SKILL, increment, capped = false)
        assertEquals(skills.getCurrentLevel(ATTACK_SKILL), baseLevel + (increment * 2))
    }

    @Test(expected = IllegalStateException::class)
    fun `alter level illegally with different cap signum`() {
        val skills = createSkills()

        val baseLevel = 50
        val decrement = 3
        skills.setBaseLevel(ATTACK_SKILL, baseLevel)
        skills.alterCurrentLevel(ATTACK_SKILL, -decrement, decrement)
        assertEquals(skills.getCurrentLevel(ATTACK_SKILL), baseLevel)
    }

    @Suppress("unused")
    companion object {
        private const val SKILL_COUNT = 24

        private const val ATTACK_SKILL = 0
        private const val DEFENCE_SKILL = 1
        private const val STRENGTH_SKILL = 2
        private const val HITPOINTS_SKILL = 3
        private const val RANGED_SKILL = 4
        private const val PRAYER_SKILL = 5
        private const val MAGIC_SKILL = 6
        private const val COOKING_SKILL = 7
        private const val WOODCUTTING_SKILL = 8
        private const val FLETCHING_SKILL = 9
        private const val FISHING_SKILL = 10
        private const val FIREMAKING_SKILL = 11
        private const val CRAFTING_SKILL = 12
        private const val SMITHING_SKILL = 13
        private const val MINING_SKILL = 14
        private const val HERBLORE_SKILL = 15
        private const val AGILITY_SKILL = 16
        private const val THIEVING_SKILL = 17
        private const val SLAYER_SKILL = 18
        private const val FARMING_SKILL = 19
        private const val RUNECRAFTING_SKILL = 20
        private const val HUNTER_SKILL = 21
        private const val CONSTRUCTION_SKILL = 22
    }
}