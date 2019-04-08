package gg.rsmod.game.model.timer

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A system responsible for storing and exposing [TimerKey]s and their associated
 * values. These values represent game cycles left for the timer to "complete".
 *
 * @author Tom <rspsmods@gmail.com>
 */
class TimerMap {

    private var timers: MutableMap<TimerKey, Int> = HashMap(0)

    operator fun get(key: TimerKey): Int = timers[key]!!

    operator fun set(key: TimerKey, value: Int): TimerMap {
        timers[key] = value
        return this
    }

    fun has(key: TimerKey): Boolean = timers.getOrDefault(key, 0) > 0

    fun exists(key: TimerKey): Boolean = timers.containsKey(key)

    fun remove(key: TimerKey) {
        timers.remove(key)
    }

    fun clear() {
        timers.clear()
    }

    fun toPersistentTimers(): List<PersistentTimer> = timers.filter { it.key.persistenceKey != null }.map { PersistentTimer(it.key.persistenceKey, it.key.tickOffline, it.value, System.currentTimeMillis()) }

    fun getTimers(): MutableMap<TimerKey, Int> = timers

    val isNotEmpty: Boolean
        get() = timers.isNotEmpty()

    /**
     * Represents a persistent timer that will be saved through player sessions.
     */
    data class PersistentTimer(@JsonProperty("identifier") val identifier: String? = null,
                               @JsonProperty("tickOffline") val tickOffline: Boolean = true,
                               @JsonProperty("timeLeft") val timeLeft: Int,
                               @JsonProperty("currentMs") val currentMs: Long)
}