package gg.rsmod.game.model

/**
 * A system responsible for storing and exposing [AttributeKey]s and their
 * associated values. The type of the key is inferred by the [AttributeKey]
 * used when putting or getting the value.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class AttributeSystem {

    /**
     * Temporary attributes that can be attached to our system.
     * We do not initialize it as this system can be used for entities such as
     * [gg.rsmod.game.model.entity.GameObject]s, of which there can be millions
     * of in the world at a time. The amount of overhead wouldn't be worth it as
     * most of these objects won't even use attributes.
     */
    private var attributes: HashMap<AttributeKey<*>, Any>? = null

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: AttributeKey<T>): T {
        constructIfNeeded()
        return (attributes!![key] as? T)!!
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getNullable(key: AttributeKey<T>): T? {
        constructIfNeeded()
        return (attributes!![key] as? T)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrDefault(key: AttributeKey<T>, default: T): T {
        constructIfNeeded()
        return (attributes!![key] as? T) ?: default
    }

    operator fun <T> set(key: AttributeKey<T>, value: T) {
        put(key, value)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> put(key: AttributeKey<T>, value: T): AttributeSystem {
        constructIfNeeded()
        attributes!![key] = value as Any
        return this
    }

    fun remove(key: AttributeKey<*>) {
        constructIfNeeded()
        attributes!!.remove(key)
    }

    fun has(key: AttributeKey<*>): Boolean = attributes?.containsKey(key) ?: false

    private fun constructIfNeeded() {
        if (attributes == null) {
            attributes = hashMapOf()
        }
    }
}