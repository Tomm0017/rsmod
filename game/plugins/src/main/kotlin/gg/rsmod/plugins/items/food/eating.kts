package gg.rsmod.plugins.content.items.food

import gg.rsmod.plugins.api.ext.getInteractingItemSlot
import gg.rsmod.plugins.api.ext.player

Food.values.forEach { food ->
    on_item_option(food.item, 1) {
        val p = it.player()

        if (!Foods.canEat(p, food)) {
            return@on_item_option
        }

        val inventorySlot = it.getInteractingItemSlot()
        if (p.inventory.remove(id = food.item, beginSlot = inventorySlot).hasSucceeded()) {
            Foods.eat(p, food)
            if (food.replacement != -1) {
                p.inventory.add(item = food.replacement, beginSlot = inventorySlot)
            }
        }
    }
}