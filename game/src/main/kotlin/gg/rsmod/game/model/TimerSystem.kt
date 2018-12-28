package gg.rsmod.game.model

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

    fun remove(key: TimerKey) {
        constructIfNeeded()
        timers!!.remove(key)
    }

    private fun constructIfNeeded() {
        if (timers == null) {
            timers = hashMapOf()
        }
    }

    fun getPersistentTimers(): Map<TimerKey, Int> = timers?.filter { it.key.identifier != null } ?: emptyMap()
}