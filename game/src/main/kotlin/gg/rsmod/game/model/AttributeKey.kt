package gg.rsmod.game.model

import com.google.common.base.MoreObjects

/**
 * An [AttributeKey] is a flexible key that can be used to represent any type of
 * value.
 *
 * @param T
 * The type of the value that this attribute will store.
 *
 * <strong>Note</strong>- do not use Double or Float if your key needs to be
 * persistent ([persistenceKey] != null). Not all saving and loading systems
 * would be able to differentiate doubles and ints when being loaded, so for
 * compatibility reasons, it is preferable to use an [Int] instead and just
 * multiply or divide it accordingly to represent a [Double] or [Float], if
 * needed.
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
        return if (persistenceKey != null) other.persistenceKey == persistenceKey else super.equals(other)
    }

    override fun hashCode(): Int = persistenceKey?.hashCode() ?: super.hashCode()
}