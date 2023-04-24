package gg.rsmod.game.model.attr

import com.google.common.base.MoreObjects
import gg.rsmod.game.model.entity.Pawn
import kotlin.reflect.KProperty

/**
 * An [AttributeKey] is a flexible key that can be used to represent any type of
 * value.
 *
 * Can also be used as a delegate for a [Pawn] extension property
 *
 * @param T
 * The type of the value that this attribute will store.
 *
 * @param persistenceKey
 * A string key that will be used for persistence. If [persistenceKey] != null,
 * the value of this key in an object's [AttributeMap] will persist (be saved).
 *
 * <strong>Note</strong>- do not use Double or Float if your key needs to be
 * persistent ([persistenceKey] != null). Not all saving and loading systems
 * would be able to differentiate doubles and ints when being loaded, so for
 * compatibility reasons, it is preferable to use an [Int] instead and just
 * multiply or divide it accordingly to represent a [Double] or [Float], if
 * needed.
 *
 * @param resetOnDeath if true, the timer will be removed on pawn death.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class AttributeKey<T>(val persistenceKey: String? = null, val resetOnDeath: Boolean = false) {

    operator fun getValue(ref: Pawn, prop: KProperty<*>): T = ref.attr[this] as T

    operator fun setValue(ref: Pawn, prop: KProperty<*>, value: T) { ref.attr[this] = value }

    /**
     * For when you want to delegate to this key but also want a default value just in case.
     */
    operator fun invoke(defaultValue: T) = AttributeDelegate(this, defaultValue)

    override fun toString(): String = MoreObjects.toStringHelper(this).add("persistenceKey", persistenceKey).add("resetOnDeath", resetOnDeath).toString()

    override fun equals(other: Any?): Boolean {
        if (other !is AttributeKey<*>) {
            return false
        }
        return if (persistenceKey != null) {
            other.persistenceKey == persistenceKey && other.resetOnDeath == resetOnDeath
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int = persistenceKey?.hashCode() ?: super.hashCode()
}