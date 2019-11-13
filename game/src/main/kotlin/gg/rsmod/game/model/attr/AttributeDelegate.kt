package gg.rsmod.game.model.attr

import gg.rsmod.game.model.entity.Pawn
import kotlin.reflect.KProperty

/**
 * A delegate class meant for delegating [Pawn] extension properties to.
 *
 * @property [attributeKey] The [AttributeKey] being delegated to
 *
 * @property [defaultValue] The default value when [getValue] is called but [Pawn.attr] fails to return a [T]
 */
class AttributeDelegate<T>(val attributeKey: AttributeKey<T>, private val defaultValue: T) {
    constructor(persistenceKey: String? = null, resetOnDeath: Boolean = false, defaultValue: T): this(
            attributeKey = AttributeKey<T>(persistenceKey, resetOnDeath),
            defaultValue = defaultValue
    )

    operator fun getValue(pawn: Pawn, prop: KProperty<*>): T = pawn.attr[attributeKey] ?: defaultValue

    operator fun setValue(pawn: Pawn, prop: KProperty<*>, newValue: T) { pawn.attr[attributeKey] = newValue }
}
