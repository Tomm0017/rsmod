
import gg.rsmod.plugins.osrs.api.helper.getInteractingItemSlot
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.content.items.food.Food
import gg.rsmod.plugins.osrs.content.items.food.Foods

Food.values().forEach { food ->
    onItemOption(food.item, 1) {
        val p = it.player()

        if (!Foods.canEat(p, food)) {
            return@onItemOption
        }

        val inventorySlot = it.getInteractingItemSlot()
        if (p.inventory.remove(id = food.item, beginSlot = inventorySlot).hasSucceeded()) {
            Foods.eat(p, food)
            if (food.replacement != -1) {
                p.inventory.add(id = food.replacement, beginSlot = inventorySlot)
            }
        }
    }
}