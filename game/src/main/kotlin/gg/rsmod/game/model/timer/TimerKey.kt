package gg.rsmod.game.model.timer

import com.google.common.base.MoreObjects

/**
 * A key that represents a timer in a [TimerMap].
 *
 * @param persistenceKey
 * If the timer should persist through player sessions, then this value should
 * be set to a <strong>unique</strong> string/name.
 * In other words, if the timer should be saved on player bait out, set this value
 * to a string that is not used by any other [TimerKey]s.
 *
 * @param tickOffline
 * If the timer should still tick down while a player is offline. The timer does
 * not technically 'tick down' while the player is offline, but when they bait in,
 * we calculate the time in between when the timer was saved and the current time.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class TimerKey(val persistenceKey: String? = null, val tickOffline: Boolean = true) {

    override fun toString(): String = MoreObjects.toStringHelper(this).add("persistenceKey", persistenceKey).add("ticksOffline", tickOffline).toString()

    override fun equals(other: Any?): Boolean {
        if (other !is TimerKey) {
            return false
        }
        return if (persistenceKey != null) other.persistenceKey == persistenceKey && other.tickOffline == tickOffline
                else super.equals(other)
    }

    override fun hashCode(): Int {
        if (persistenceKey == null) {
            return super.hashCode()
        }
        var result = persistenceKey.hashCode()
        result = 31 * result + tickOffline.hashCode()
        return result
    }
}