package gg.rsmod.game.model

/**
 * An system responsible for storing and exposing [AttributeKey]s and their
 * associated values. The type of the key is inferred by the [AttributeKey]
 * used when putting or getting the value.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class AttributeSystem(private val attributes: HashMap<AttributeKey<*>, Any> = hashMapOf()) {

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: AttributeKey<T>): T = (attributes[key] as? T)!!

    @Suppress("UNCHECKED_CAST")
    fun <T> getNullable(key: AttributeKey<T>): T? = (attributes[key] as? T)

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrDefault(key: AttributeKey<T>, default: T): T = (attributes[key] as? T) ?: default

    @Suppress("UNCHECKED_CAST")
    fun <T> put(key: AttributeKey<T>, value: T): AttributeSystem {
        attributes[key] = value as Any
        return this
    }

    fun remove(key: AttributeKey<*>) {
        attributes.remove(key)
    }
}