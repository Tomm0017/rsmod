package gg.rsmod.plugins.content.skills.cooking.special

val INGREDIENTS = intArrayOf(Items.EGG, Items.POT_OF_FLOUR, Items.BUCKET_OF_MILK)

INGREDIENTS.forEach { ingredient ->
    on_item_on_item(Items.CAKE_TIN, ingredient) {
        if(!checkIngredients(player)) {
            return@on_item_on_item
        }
        createUncookedCake(player)
    }
}

fun checkIngredients(player: Player): Boolean {
    val defs = world.definitions
    val names = INGREDIENTS.associate { it to defs.get(ItemDef::class.java, it).name.toLowerCase() }

    val missing = arrayListOf<Int>()
    INGREDIENTS.forEach { ingredient ->
        if(!player.inventory.contains(ingredient)) {
            missing.add(ingredient)
        }
    }

    if(missing.size > 0) {
        var msg: String
        if(missing.size == 1) { msg = "You also need ${names[missing.get(0)]} to make a cake." }
        else { msg = "You also need ${names[missing.get(0)]} and ${names[missing.get(1)]} to make a cake." }
        player.queue { messageBox(msg) }
        return false
    }

    if(player.getSkills().getCurrentLevel(Skills.COOKING) < 40) {
       player.filterableMessage("You need at least ${Skills.getSkillName(player.world, Skills.COOKING)} level of 40 to make a cake.")
        return false
    }
    return true
}

fun createUncookedCake(player: Player) {
    player.inventory.remove(Items.CAKE_TIN)
    player.inventory.remove(Items.EGG)
    player.inventory.remove(Items.POT_OF_FLOUR)
    player.inventory.remove(Items.BUCKET_OF_MILK)

    player.inventory.add(Items.POT)
    player.inventory.add(Items.EMPTY_BUCKET)
    player.inventory.add(Items.UNCOOKED_CAKE)

    player.filterableMessage("You mix the ingredients together into the cake tin.")
}