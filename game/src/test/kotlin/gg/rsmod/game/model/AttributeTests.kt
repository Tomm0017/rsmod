package gg.rsmod.game.model

import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.attr.AttributeMap
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author Tom <rspsmods@gmail.com>
 */
class AttributeTests {

    @Test
    fun persistenceTests() {
        val attributes = AttributeMap()

        val key1 = AttributeKey<String>(persistenceKey = "a_string_attr")
        val key2 = AttributeKey<String>(persistenceKey = "a_string_attr")
        val key3 = AttributeKey<Int>(persistenceKey = "an_int_attr")
        val key4 = AttributeKey<String>()

        attributes[key1] = "key value"

        assertTrue(attributes.has(key1))
        assertTrue(attributes.has(key2))
        assertFalse(attributes.has(key3))
        assertFalse(attributes.has(key4))
    }

    @Test
    fun uniqueTests() {
        val attributes = AttributeMap()

        val key1 = AttributeKey<String>()
        val key2 = AttributeKey<String>()

        attributes[key1] = "key value"

        assertTrue(attributes.has(key1))
        assertFalse(attributes.has(key2))

        attributes[key2] = "key value 2"

        assertTrue(attributes.has(key1))
        assertTrue(attributes.has(key2))
    }
}