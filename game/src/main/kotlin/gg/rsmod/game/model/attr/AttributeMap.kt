package gg.rsmod.game.model.attr

/**
 * A system responsible for storing and exposing [AttributeKey]s and their
 * associated values. The type of the key is inferred by the [AttributeKey]
 * used when putting or getting the value.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class AttributeMap {

    private var attributes: MutableMap<AttributeKey<*>, Any> = HashMap(0)

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: AttributeKey<T>): T? = (attributes[key] as? T)

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrDefault(key: AttributeKey<T>, default: T): T = (attributes[key] as? T) ?: default

    @Suppress("UNCHECKED_CAST")
    fun <T> put(key: AttributeKey<T>, value: T): AttributeMap {
        attributes[key] = value as Any
        return this
    }

    operator fun <T> set(key: AttributeKey<T>, value: T) {
        put(key, value)
    }

    fun remove(key: AttributeKey<*>) {
        attributes.remove(key)
    }

    fun has(key: AttributeKey<*>): Boolean = attributes.containsKey(key)

    fun clear() {
        attributes.clear()
    }

    fun toPersistentMap(): Map<String, Any> = attributes.filterKeys { it.persistenceKey != null }.mapKeys { it.key.persistenceKey!! }
}