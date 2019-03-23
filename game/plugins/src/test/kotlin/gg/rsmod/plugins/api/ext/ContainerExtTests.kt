package gg.rsmod.plugins.api.ext

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.item.ItemAttribute
import net.runelite.cache.fs.Store
import org.junit.BeforeClass
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ContainerExtTests {

    @Test
    fun `verify that a transfer goes as expected`() {
        val container1 = ItemContainer(definitions, capacity = 28, stackType = ContainerStackType.NORMAL)
        val container2 = ItemContainer(definitions, capacity = 28, stackType = ContainerStackType.NORMAL)

        val item = Item(4151)

        assert((container1.transfer(container2, item)?.completed ?: 0) == 0)

        container1.add(item)
        assert((container1.transfer(container2, item)?.completed ?: 0) == 1)
    }

    @Test
    fun `verify that transferred item keeps its attributes`() {
        val container1 = ItemContainer(definitions, capacity = 28, stackType = ContainerStackType.NORMAL)
        val container2 = ItemContainer(definitions, capacity = 28, stackType = ContainerStackType.NORMAL)

        val item = Item(4151)
        val charges = 40

        container1.set(0, item.putAttr(ItemAttribute.CHARGES, charges))

        assertNotNull(container1[0])
        assert(container1[0]!!.getAttr(ItemAttribute.CHARGES) == charges)

        container1.transfer(container2, container1[0]!!)

        assertNull(container1[0])
        assertNotNull(container2[0])

        assert(container2[0]!!.getAttr(ItemAttribute.CHARGES) == charges)
    }

    @Test
    fun `verify failed transfer due to full container`() {
        val container1 = ItemContainer(definitions, capacity = 28, stackType = ContainerStackType.NORMAL)
        val container2 = ItemContainer(definitions, capacity = 0, stackType = ContainerStackType.NORMAL)

        val item = Item(4151)

        container1.set(0, item)

        assertNotNull(container1[0])

        val transfer = container1.transfer(container2, container1[0]!!)
        assert((transfer?.completed ?: 0) == 0)

        assert(container1[0] == item)
        assert(container2.occupiedSlotCount == 0)
    }

    companion object {
        private val definitions = DefinitionSet()

        private lateinit var store: Store

        @BeforeClass
        @JvmStatic
        fun loadCache() {
            val path = Paths.get("./../../data", "cache")
            check(Files.exists(path)) { "Path does not exist: ${path.toAbsolutePath()}" }

            store = Store(path.toFile())
            store.load()

            definitions.load(store, ItemDef::class.java)

            assertNotEquals(definitions.getCount(ItemDef::class.java), 0)
        }
    }
}
