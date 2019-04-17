package gg.rsmod.game.model.entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcStatsTests {

    private fun createStats(): Npc.Stats = Npc.Stats(STATS_COUNT)

    private fun Npc.Stats.setBaseLevel(skill: Int, level: Int) {
        setMaxLevel(skill, level)
        setCurrentLevel(skill, level)
    }

    private fun Npc.Stats.restore(skill: Int) {
        setCurrentLevel(skill, getMaxLevel(skill))
    }

    @Test
    fun `alter levels (capped)`() {
        val skills = createStats()

        val baseLevel = 50
        skills.setBaseLevel(ATTACK_STAT, baseLevel)
        assertEquals(skills.getCurrentLevel(ATTACK_STAT), baseLevel)
        assertEquals(skills.getMaxLevel(ATTACK_STAT), baseLevel)

        val decrement = 3
        skills.decrementCurrentLevel(ATTACK_STAT, decrement, capped = true)
        assertEquals(baseLevel - decrement, skills.getCurrentLevel(ATTACK_STAT))
        assertEquals(baseLevel, skills.getMaxLevel(ATTACK_STAT))

        skills.decrementCurrentLevel(ATTACK_STAT, decrement, capped = true)
        assertEquals(baseLevel - decrement, skills.getCurrentLevel(ATTACK_STAT))

        skills.restore(ATTACK_STAT)
        assertEquals(baseLevel, skills.getCurrentLevel(ATTACK_STAT))

        val increment = 3
        skills.incrementCurrentLevel(ATTACK_STAT, increment, capped = true)
        assertEquals(baseLevel + increment, skills.getCurrentLevel(ATTACK_STAT))
        assertEquals(baseLevel, skills.getMaxLevel(ATTACK_STAT))

        skills.incrementCurrentLevel(ATTACK_STAT, increment, capped = true)
        assertEquals(baseLevel + increment, skills.getCurrentLevel(ATTACK_STAT))
    }

    @Test
    fun `alter levels (uncapped)`() {
        val skills = createStats()

        val baseLevel = 50
        skills.setBaseLevel(ATTACK_STAT, baseLevel)
        assertEquals(skills.getCurrentLevel(ATTACK_STAT), baseLevel)
        assertEquals(skills.getMaxLevel(ATTACK_STAT), baseLevel)

        val decrement = 3
        skills.decrementCurrentLevel(ATTACK_STAT, decrement, capped = false)
        assertEquals(skills.getCurrentLevel(ATTACK_STAT), baseLevel - decrement)
        assertEquals(skills.getMaxLevel(ATTACK_STAT), baseLevel)

        skills.decrementCurrentLevel(ATTACK_STAT, decrement, capped = false)
        assertEquals(skills.getCurrentLevel(ATTACK_STAT), baseLevel - (decrement * 2))

        skills.restore(ATTACK_STAT)
        assertEquals(skills.getCurrentLevel(ATTACK_STAT), baseLevel)

        val increment = 3
        skills.incrementCurrentLevel(ATTACK_STAT, increment, capped = false)
        assertEquals(skills.getCurrentLevel(ATTACK_STAT), baseLevel + increment)
        assertEquals(skills.getMaxLevel(ATTACK_STAT), baseLevel)

        skills.incrementCurrentLevel(ATTACK_STAT, increment, capped = false)
        assertEquals(skills.getCurrentLevel(ATTACK_STAT), baseLevel + (increment * 2))
    }

    @Test(expected = IllegalStateException::class)
    fun `alter level illegally with different cap signum`() {
        val skills = createStats()

        val baseLevel = 50
        val decrement = 3
        skills.setBaseLevel(ATTACK_STAT, baseLevel)
        skills.alterCurrentLevel(ATTACK_STAT, -decrement, decrement)
        assertEquals(skills.getCurrentLevel(ATTACK_STAT), baseLevel)
    }

    @Suppress("unused")
    companion object {
        private const val STATS_COUNT = 5

        const val ATTACK_STAT = 0
        const val STRENGTH_STAT = 1
        const val DEFENCE_STAT = 2
        const val MAGIC_STAT = 3
        const val RANGED_STAT = 4
    }
}