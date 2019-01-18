package gg.rsmod.plugins.osrs.content.inter.priceguide

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.AttributeKey
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.InterfacePane
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.service.item.ItemValueService
import java.text.DecimalFormat

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PriceGuide {

    const val INTERFACE_ID = 464
    const val TAB_INTERFACE_ID = 238

    private val GUIDE_CONTAINER = AttributeKey<ItemContainer>()
    private val FAKE_INV_CONTAINER = AttributeKey<ItemContainer>()

    fun open(p: Player) {
        p.attr.put(GUIDE_CONTAINER, ItemContainer(p.world.definitions, p.inventory.capacity, ContainerStackType.STACK))
        p.attr.put(FAKE_INV_CONTAINER, ItemContainer(p.inventory))

        p.setMainInterfaceBackground(color = -1, transparency = -1)
        p.openInterface(interfaceId = INTERFACE_ID, pane = InterfacePane.MAIN_SCREEN)
        p.openInterface(interfaceId = TAB_INTERFACE_ID, pane = InterfacePane.TAB_AREA)

        update(p)

        p.setInterfaceSetting(parent = INTERFACE_ID, child = 2, range = 0..27, setting = 1086)
        p.invokeScript(149, 15597568, 93, 4, 7, 0, -1, "Add<col=ff9040>", "Add-5<col=ff9040>", "Add-10<col=ff9040>", "Add-All<col=ff9040>", "Add-X<col=ff9040>")
        p.setInterfaceSetting(parent = TAB_INTERFACE_ID, child = 0, range = 0..27, setting = 1086)
    }

    fun close(p: Player) {
        p.closeInterface(PriceGuide.TAB_INTERFACE_ID)
        p.attr.remove(GUIDE_CONTAINER)
        p.attr.remove(FAKE_INV_CONTAINER)
    }

    fun add(p: Player, fakeInvSlot: Int) {
        val container = p.attr[FAKE_INV_CONTAINER] ?: return
        val item = container[fakeInvSlot] ?: return
        add(p, item.id, item.amount)
    }

    fun add(p: Player, item: Int, amount: Int) {
        val guideContainer = p.attr[GUIDE_CONTAINER] ?: return
        val invContainer = p.attr[FAKE_INV_CONTAINER] ?: return

        if (!p.world.definitions.get(ItemDef::class.java, item).isTradeable()) {
            p.message("You cannot trade that item.")
            return
        }

        val remove = invContainer.remove(item, amount, assureFullRemoval = false)
        if (remove.completed == 0) {
            return
        }

        val add = guideContainer.add(item, remove.completed, assureFullInsertion = false)

        if (add.completed == 0) {
            p.message("The price guide has no free space left.")
            return
        }

        if (add.getLeftOver() > 0) {
            invContainer.add(item, add.getLeftOver(), assureFullInsertion = false)
        }

        update(p)
    }

    fun depositInventory(p: Player) {
        val guideContainer = p.attr[GUIDE_CONTAINER] ?: return
        val invContainer = p.attr[FAKE_INV_CONTAINER] ?: return

        var any = false
        var anyUntradeables = false

        for (i in 0 until invContainer.capacity) {
            val item = invContainer[i] ?: continue

            if (!p.world.definitions.get(ItemDef::class.java, item.id).isTradeable()) {
                anyUntradeables = true
                continue
            }

            val remove = invContainer.remove(item.id, item.amount, assureFullRemoval = false)
            if (remove.completed == 0) {
                continue
            }
            val add = guideContainer.add(item.id, remove.completed, assureFullInsertion = false)
            if (add.completed == 0) {
                continue
            }
            if (add.getLeftOver() > 0) {
                invContainer.add(item.id, add.getLeftOver(), assureFullInsertion = false)
            }
            any = true
        }

        if (any) {
            update(p)
        } else if (anyUntradeables) {
            p.message("You have items that cannot be traded.")
        } else {
            p.message("You have no items that can be checked.")
        }
    }

    fun remove(p: Player, slot: Int, amount: Int) {
        val guideContainer = p.attr[GUIDE_CONTAINER] ?: return
        val invContainer = p.attr[FAKE_INV_CONTAINER] ?: return

        val item = guideContainer[slot] ?: return

        val remove = guideContainer.remove(item.id, amount, assureFullRemoval = false)
        if (remove.completed == 0) {
            return
        }

        val add = invContainer.add(item.id, remove.completed, assureFullInsertion = false)
        if (add.completed == 0) {
            p.message("You don't have enough free inventory space to do that.")
            return
        }

        if (add.getLeftOver() > 0) {
            guideContainer.add(item.id, add.getLeftOver(), assureFullInsertion = false)
        }

        update(p)
    }

    suspend fun remove(it: Plugin, slot: Int, opt: Int) {
        val p = it.player()
        val container = p.attr[GUIDE_CONTAINER] ?: return
        val item = container[slot] ?: return

        val amount = when (opt) {
            0 -> 1
            1 -> 5
            2 -> 10
            3 -> container.getItemCount(item.id)
            4 -> it.inputInteger()
            9 -> {
                p.world.sendExamine(p, item.id, ExamineEntityType.ITEM)
                return
            }
            else -> return
        }
        remove(p = p, slot = slot, amount = amount)
    }

    suspend fun add(it: Plugin, slot: Int, opt: Int) {
        val p = it.player()
        val container = p.attr[FAKE_INV_CONTAINER] ?: return
        val item = container[slot] ?: return

        val amount = when (opt) {
            0 -> 1
            1 -> 5
            2 -> 10
            3 -> container.getItemCount(item.id)
            4 -> it.inputInteger()
            9 -> {
                p.world.sendExamine(p, item.id, ExamineEntityType.ITEM)
                return
            }
            else -> return
        }
        add(p = p, item = item.id, amount = amount)
    }

    fun search(p: Player, item: Int) {
        val def = p.world.definitions.get(ItemDef::class.java, item)
        val valueService = p.world.getService(ItemValueService::class.java).orElse(null)
        val cost = valueService?.get(item) ?: def.cost

        p.setInterfaceItem(parent = INTERFACE_ID, child = 8, item = item, amountOrZoom = 1)
        p.invokeScript(600, 0, 1, 15, 30408716)
        p.setInterfaceText(parent = INTERFACE_ID, child = 12, text = "${def.name}:<br><col=ffffff>${DecimalFormat().format(cost)}</col>")
    }

    fun update(p: Player) {
        val guideContainer = p.attr[GUIDE_CONTAINER]!!
        val invContainer = p.attr[FAKE_INV_CONTAINER]!!

        p.sendContainer(key = 90, container = guideContainer)
        p.sendContainer(key = 93, container = invContainer)

        p.setInterfaceItem(parent = INTERFACE_ID, child = 8, item = -1, amountOrZoom = 1)

        val valueService = p.world.getService(ItemValueService::class.java).orElse(null)
        val costs = Array(size = guideContainer.capacity) { 0 }
        guideContainer.forEachIndexed { index, item ->
            if (item != null) {
                val cost = valueService?.get(item.id) ?: p.world.definitions.get(ItemDef::class.java, item.id).cost
                costs[index] = cost
            }
        }

        p.invokeScript(785, *costs)
        p.invokeScript(600, 1, 1, 15, 30408716)

        p.setInterfaceText(parent = INTERFACE_ID, child = 12, text = "Total guide price:<br><col=ffffff>${DecimalFormat().format(guideContainer.networth(p.world))}</col>")
    }
}