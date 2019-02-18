package gg.rsmod.game.model.timer

import com.google.common.base.MoreObjects

/**
 * A value used for [TimerSystem] as a key.
 *
 * @see TimerSystem
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