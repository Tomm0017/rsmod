package gg.rsmod.game.model

/**
 * An system responsible for storing and exposing [AttributeKey]s and their
 * associated values. The type of the key is inferred by the [AttributeKey]
 * used when putting or getting the value.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class AttributeSystem {

    /**
     * Temporary attributes that can be attached to our system.
     */
    private val attributes: HashMap<AttributeKey<*>, Any> = hashMapOf()

    /**
     * Persistent attributes which must be saved from our system and loaded
     * when needed. This map does not support storing [Double]s as we convert
     * every double into an [Int] when loading. This is done because some
     * parsers can interpret [Number]s differently, so we want to force every
     * [Number] to an [Int], explicitly. If you wish to store a [Double], you
     * can multiply your value by [100] and then divide it on login as a work-
     * around.
     */
    private val persistent: HashMap<String, Any> = hashMapOf()

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

    @Suppress("UNCHECKED_CAST")
    fun <T> getPersistent(key: String): T = (persistent[key] as? T)!!

    @Suppress("UNCHECKED_CAST")
    fun <T> getPersistentNullable(key: String): T? = (persistent[key] as? T)

    @Suppress("UNCHECKED_CAST")
    fun <T> getPersistentOrDefault(key: String, default: T): T = (persistent[key] as? T) ?: default

    @Suppress("UNCHECKED_CAST")
    fun <T> putPersistent(key: String, value: T): AttributeSystem {
        persistent[key] = value as Any
        return this
    }

    fun removePersistent(key: String) {
        persistent.remove(key)
    }

    /**
     * Should only be used when saving [persistent] attributes.
     */
    fun __getPersistentMap(): HashMap<String, Any> = persistent
}