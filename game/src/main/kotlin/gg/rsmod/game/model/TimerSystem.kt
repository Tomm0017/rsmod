package gg.rsmod.game.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Tom <rspsmods@gmail.com>
 */
class TimerSystem {

    /**
     * Temporary timers that can be attached to our system.
     * We do not initialize it as this system can be used for entities such as
     * [gg.rsmod.game.model.entity.GameObject]s, of which there can be millions
     * of in the world at a time. The amount of overhead from always initializing
     * this map wouldn't be worth the cost as most of these objects won't use
     * timers.
     */
    private var timers: HashMap<TimerKey, Int>? = null

    @Suppress("UNCHECKED_CAST")
    operator fun get(key: TimerKey): Int {
        constructIfNeeded()
        return (timers!![key]!!)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun set(key: TimerKey, value: Int): TimerSystem {
        constructIfNeeded()
        timers!![key] = value
        return this
    }

    fun has(key: TimerKey): Boolean = timers?.containsKey(key) ?: false

    fun remove(key: TimerKey) {
        constructIfNeeded()
        timers!!.remove(key)
    }

    private fun constructIfNeeded() {
        if (timers == null) {
            timers = hashMapOf()
        }
    }

    fun toPersistentTimers(): List<PersistentTimer> = timers?.filter { it.key.identifier != null }?.map { PersistentTimer(it.key.identifier, it.key.tickOffline, it.value, System.currentTimeMillis()) } ?: emptyList()

    data class PersistentTimer(@JsonProperty("identifier") val identifier: String? = null,
                               @JsonProperty("tickOffline") val tickOffline: Boolean = true,
                               @JsonProperty("timeLeft") val timeLeft: Int,
                               @JsonProperty("currentMs") val currentMs: Long)
}