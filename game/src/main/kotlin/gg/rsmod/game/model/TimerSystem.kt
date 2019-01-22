package gg.rsmod.game.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A system responsible for storing and exposing [TimerKey]s and their associated
 * values. These values represent game cycles left for the timer to "complete".
 *
 * @author Tom <rspsmods@gmail.com>
 */
class TimerSystem {

    /**
     * Timers that can be attached to our system.
     *
     * We do not initialize it as this system can be used for entities such as
     * [gg.rsmod.game.model.entity.GameObject]s, of which there can be millions
     * of in the world at a time. The amount of overhead from always initializing
     * this map wouldn't be worth the cost as most of these objects won't use
     * timers.
     */
    private var timers: HashMap<TimerKey, Int>? = null

    operator fun get(key: TimerKey): Int {
        constructIfNeeded()
        return (timers!![key]!!)
    }

    operator fun set(key: TimerKey, value: Int): TimerSystem {
        constructIfNeeded()
        timers!![key] = value
        return this
    }

    fun has(key: TimerKey): Boolean = (timers?.getOrDefault(key, 0) ?: 0) > 0

    fun exists(key: TimerKey): Boolean = timers?.containsKey(key) ?: false

    fun remove(key: TimerKey) {
        constructIfNeeded()
        timers!!.remove(key)
    }

    private fun constructIfNeeded() {
        if (timers == null) {
            timers = hashMapOf()
        }
    }

    fun toPersistentTimers(): List<PersistentTimer> = timers?.filter { it.key.persistenceKey != null }?.map { PersistentTimer(it.key.persistenceKey, it.key.tickOffline, it.value, System.currentTimeMillis()) } ?: emptyList()

    fun getTimers(): MutableMap<TimerKey, Int> = timers ?: HashMap(0)

    data class PersistentTimer(@JsonProperty("identifier") val identifier: String? = null,
                               @JsonProperty("tickOffline") val tickOffline: Boolean = true,
                               @JsonProperty("timeLeft") val timeLeft: Int,
                               @JsonProperty("currentMs") val currentMs: Long)
}