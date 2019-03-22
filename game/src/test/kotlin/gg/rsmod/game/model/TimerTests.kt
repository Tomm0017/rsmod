package gg.rsmod.game.model

import gg.rsmod.game.model.timer.TimerKey
import gg.rsmod.game.model.timer.TimerMap
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author Tom <rspsmods@gmail.com>
 */
class TimerTests {

    @Test
    fun persistenceTests() {
        val timers = TimerMap()

        val key1 = TimerKey(persistenceKey = "persistent", tickOffline = true)
        val key2 = TimerKey(persistenceKey = "persistent", tickOffline = true)
        val key3 = TimerKey(persistenceKey = "persistent", tickOffline = false)
        val key4 = TimerKey()

        timers[key1] = 4

        assertTrue(timers.exists(key1))
        assertTrue(timers.exists(key2))
        assertFalse(timers.exists(key3))
        assertFalse(timers.exists(key4))
    }

    @Test
    fun uniqueTests() {
        val timers = TimerMap()

        val key1 = TimerKey()
        val key2 = TimerKey()

        timers[key1] = 4

        assertTrue(timers.exists(key1))
        assertFalse(timers.exists(key2))

        timers[key2] = 6

        assertTrue(timers.exists(key1))
        assertTrue(timers.exists(key2))
    }
}