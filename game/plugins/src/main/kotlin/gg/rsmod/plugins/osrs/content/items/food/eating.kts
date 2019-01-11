import gg.rsmod.game.model.INTERACTING_ITEM_SLOT
import gg.rsmod.plugins.osrs.content.items.food.Food
import gg.rsmod.plugins.osrs.content.items.food.Foods
import gg.rsmod.plugins.osrs.api.player

/**
 * @author Tom <rspsmods@gmail.com>
 */

Food.values().forEach { food ->
    r.bindItem(food.item, 1) {
        val p = it.player()

        if (!Foods.canEat(p, food)) {
            return@bindItem
        }

        val inventorySlot = it.player().attr[INTERACTING_ITEM_SLOT]
        if (p.inventory.remove(id = food.item, beginSlot = inventorySlot).hasSucceeded()) {
            Foods.eat(p, food)
            if (food.replacement != -1) {
                p.inventory.add(id = food.replacement, beginSlot = inventorySlot)
            }
        }
    }
}