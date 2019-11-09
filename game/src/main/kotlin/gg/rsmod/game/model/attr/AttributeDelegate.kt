package gg.rsmod.game.model.attr

import gg.rsmod.game.model.entity.Pawn
import kotlin.reflect.KProperty

class AttributeDelegate<T>(val attributeKey: AttributeKey<T>, private val defaultValue: T? = null) {
    constructor(persistenceKey: String? = null, resetOnDeath: Boolean = false, defaultValue: T? = null): this(
            attributeKey = AttributeKey<T>(persistenceKey, resetOnDeath),
            defaultValue = defaultValue
    )

    operator fun getValue(pawn: Pawn, prop: KProperty<*>): T =
            pawn.attr[attributeKey] ?:
            defaultValue ?:
            throw Exception("Attribute not found in pawn $pawn and has no default value")

    operator fun setValue(pawn: Pawn, prop: KProperty<*>, newValue: T) { pawn.attr[attributeKey] = newValue }
}