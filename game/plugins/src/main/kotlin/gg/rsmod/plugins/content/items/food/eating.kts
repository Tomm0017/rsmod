package gg.rsmod.plugins.content.items.food

Food.values.forEach { food ->
    on_item_option(item = food.item, option = "eat") {
        val p = player

        if (!Foods.canEat(p, food)) {
            return@on_item_option
        }

        val inventorySlot = player.getInteractingItemSlot()
        if (p.inventory.remove(item = food.item, beginSlot = inventorySlot).hasSucceeded()) {
            Foods.eat(p, food)
            if (food.replacement != -1) {
                p.inventory.add(item = food.replacement, beginSlot = inventorySlot)
            }
        }
    }
}