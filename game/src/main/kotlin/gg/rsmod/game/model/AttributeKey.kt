package gg.rsmod.game.model

import com.google.common.base.MoreObjects

/**
 * An [AttributeKey] is a flexible key that can be used to represent any type of
 * value.
 *
 * @author Tom <rspsmods@gmail.com>
 */
@Suppress("unused")
class AttributeKey<T>(val persistenceKey: String? = null) {

    override fun toString(): String = MoreObjects.toStringHelper(this).add("persistenceKey", persistenceKey).toString()

    override fun equals(other: Any?): Boolean {
        if (other !is AttributeKey<*>) {
            return false
        }
        return if (persistenceKey != null) other.persistenceKey == persistenceKey
                else super.equals(other)
    }

    override fun hashCode(): Int = persistenceKey?.hashCode() ?: super.hashCode()
}