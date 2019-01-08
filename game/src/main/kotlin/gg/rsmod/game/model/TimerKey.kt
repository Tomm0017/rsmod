package gg.rsmod.game.model

import com.google.common.base.MoreObjects

/**
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
        var result = persistenceKey?.hashCode() ?: super.hashCode()
        result = 31 * result + tickOffline.hashCode()
        return result
    }
}