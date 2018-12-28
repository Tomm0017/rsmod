package gg.rsmod.game.model

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author Tom <rspsmods@gmail.com>
 */
class TimerTests {

    @Test
    fun persistenceTests() {
        val timers = TimerSystem()

        val key1 = TimerKey(identifier = "persistent", tickOffline = true)
        val key2 = TimerKey(identifier = "persistent", tickOffline = true)
        val key3 = TimerKey(identifier = "persistent", tickOffline = false)
        val key4 = TimerKey()

        timers[key1] = 4

        assertTrue(timers.has(key1))
        assertTrue(timers.has(key2))
        assertFalse(timers.has(key3))
        assertFalse(timers.has(key4))
    }

    @Test
    fun uniqueTests() {
        val timers = TimerSystem()

        val key1 = TimerKey()
        val key2 = TimerKey()

        timers[key1] = 4

        assertTrue(timers.has(key1))
        assertFalse(timers.has(key2))

        timers[key2] = 6

        assertTrue(timers.has(key1))
        assertTrue(timers.has(key2))
    }
}