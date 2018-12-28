package gg.rsmod.game.model

import com.google.common.base.MoreObjects

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class TimerKey(val identifier: String? = null, val tickOffline: Boolean = true) {

    override fun toString(): String = MoreObjects.toStringHelper(this).add("identifier", identifier).add("ticksOffline", tickOffline).toString()

    override fun equals(other: Any?): Boolean {
        if (other !is TimerKey) {
            return false
        }
        return if (identifier != null) other.identifier == identifier && other.tickOffline == tickOffline
                else super.equals(other)
    }

    override fun hashCode(): Int {
        var result = identifier?.hashCode() ?: super.hashCode()
        result = 31 * result + tickOffline.hashCode()
        return result
    }
}