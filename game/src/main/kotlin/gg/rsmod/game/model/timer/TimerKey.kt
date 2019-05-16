package gg.rsmod.game.model.timer

import com.google.common.base.MoreObjects

/**
 * A key that represents a timer in a [TimerMap].
 *
 * @param persistenceKey if the timer should persist through player sessions,
 * then this value should be set to a <strong>unique</strong> string/name.
 * In other words, if the timer should be saved on player log out, set this
 * value to a string that is not used by any other [TimerKey]s.
 *
 * @param tickOffline if the timer should still tick down while a player is
 * offline. The timer does not technically 'tick down' while the player is
 * offline, but when they log in, we calculate the time in between when the
 * timer was saved and the current time.
 *
 * @param resetOnDeath if true, the timer will be removed on pawn death.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class TimerKey(val persistenceKey: String? = null, val tickOffline: Boolean = true, val resetOnDeath: Boolean = false) {

    override fun toString(): String = MoreObjects.toStringHelper(this).add("persistenceKey", persistenceKey).add("ticksOffline", tickOffline).add("resetOnDeath", resetOnDeath).toString()

    override fun equals(other: Any?): Boolean {
        if (other !is TimerKey) {
            return false
        }
        return if (persistenceKey != null) {
            other.persistenceKey == persistenceKey && other.tickOffline == tickOffline && other.resetOnDeath == resetOnDeath
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        if (persistenceKey == null) {
            return super.hashCode()
        }
        var result = persistenceKey.hashCode()
        result = 31 * result + tickOffline.hashCode()
        result = 31 * result + resetOnDeath.hashCode()
        return result
    }
}