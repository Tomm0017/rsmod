package gg.rsmod.game.model.container

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import net.runelite.cache.fs.Store
import org.junit.BeforeClass
import java.nio.file.Paths
import kotlin.test.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemContainerTests {

    @Test
    fun createContainer() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)

        assertEquals(container.capacity, CAPACITY)
        assertEquals(container.getNextFreeSlot(), 0)
        assertEquals(container.getFreeSlotCount(), CAPACITY)
        assertEquals(container.getOccupiedSlotCount(), 0)
    }

    @Test
    fun addNoted() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val result = container.add(item = 4152, amount = 3, forceNoStack = false, assureFullInsertion = true, beginSlot = 0)

        assertTrue(result.hasSucceeded())
        assertEquals(container.getOccupiedSlotCount(), 1)
        assertEquals(container.getNextFreeSlot(), 1)
        assertTrue(container[0] != null)
        assertEquals(container[0]!!.amount, 3)
    }

    @Test
    fun addUnnoted() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val result = container.add(item = 4151, amount = 3, forceNoStack = false, assureFullInsertion = true, beginSlot = 0)

        assertTrue(result.hasSucceeded())
        assertEquals(container.getOccupiedSlotCount(), 3)
        assertEquals(container.getNextFreeSlot(), 3)
        assertNotNull(container[0])
        assertEquals(container[0]!!.id, 4151)
        assertEquals(container[0]!!.amount, 1)
        assertNotNull(container[1])
        assertEquals(container[1]!!.id, 4151)
        assertEquals(container[1]!!.amount, 1)
        assertNotNull(container[2])
        assertEquals(container[2]!!.id, 4151)
        assertEquals(container[2]!!.amount, 1)
        assertNull(container[3])
    }

    @Test
    fun addNotedNoStack() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val result = container.add(item = 4152, amount = 3, forceNoStack = true, assureFullInsertion = true, beginSlot = 0)

        assertTrue(result.hasSucceeded())
        assertEquals(container.getOccupiedSlotCount(), 3)
        assertEquals(container.getNextFreeSlot(), 3)
        assertNotNull(container[0])
        assertEquals(container[0]!!.id, 4152)
        assertEquals(container[0]!!.amount, 1)
        assertNotNull(container[1])
        assertEquals(container[1]!!.id, 4152)
        assertEquals(container[1]!!.amount, 1)
        assertNotNull(container[2])
        assertEquals(container[2]!!.id, 4152)
        assertEquals(container[2]!!.amount, 1)
        assertNull(container[3])
    }

    @Test
    fun addOneTooManyStrict() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val result = container.add(item = 4151, amount = CAPACITY + 1, forceNoStack = false, assureFullInsertion = true, beginSlot = 0)
        assertFalse(result.hasSucceeded())
        assertEquals(container.getNextFreeSlot(), 0)
    }

    @Test
    fun addOneTooManyLoose() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val loose = container.add(item = 4151, amount = CAPACITY + 1, forceNoStack = false, assureFullInsertion = false, beginSlot = 0)
        assertFalse(loose.hasSucceeded())
        assertEquals(container.getFreeSlotCount(), 0)
    }

    @Test
    fun addOverflowAmount() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)

        container.add(item = 4152, amount = Int.MAX_VALUE, forceNoStack = false, assureFullInsertion = true, beginSlot = 0)
        assertEquals(container.getItemCount(4152), Int.MAX_VALUE)

        val result = container.add(item = 4152, amount = 1, forceNoStack = false, assureFullInsertion = true, beginSlot = 0)
        assertFalse(result.hasSucceeded())
        assertEquals(result.getLeftOver(), 1)
        assertEquals(container.getItemCount(4152), Int.MAX_VALUE)
    }

    @Test
    fun addToMiddle() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val result = container.add(item = 4151, amount = 1, forceNoStack = false, assureFullInsertion = false, beginSlot = CAPACITY / 2)
        assertTrue(result.hasSucceeded())
        assertEquals(container.getFreeSlotCount(), CAPACITY - 1)
        assertEquals(container.getNextFreeSlot(), 0)
        assertNotNull(container[CAPACITY / 2])
        assertEquals(container[CAPACITY / 2]!!.id, 4151)
    }

    @Test
    fun addToStackContainer() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.STACK)
        val result = container.add(item = 4151, amount = Int.MAX_VALUE, forceNoStack = false, assureFullInsertion = false, beginSlot = 0)

        assertTrue(result.hasSucceeded())
        assertNotNull(container[0])
        assertEquals(container[0]!!.amount, Int.MAX_VALUE)
    }

    companion object {

        private const val CAPACITY = 28

        private val definitions = DefinitionSet()

        private lateinit var store: Store

        @BeforeClass
        @JvmStatic
        fun loadCache() {
            store = Store(Paths.get("..", "data", "cache").toFile())
            store.load()

            definitions.loadAll(store)

            assertNotEquals(definitions.getCount(ItemDef::class.java), 0)
        }
    }
}
