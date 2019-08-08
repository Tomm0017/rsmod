package gg.rsmod.plugins.content.skills.cooking

import gg.rsmod.game.plugin.KotlinPlugin
import gg.rsmod.plugins.content.skills.cooking.data.CookingFood
import gg.rsmod.plugins.content.skills.cooking.data.CookingIngredient
import gg.rsmod.plugins.content.skills.cooking.data.CookingObj
import gg.rsmod.plugins.content.areas.lumbridge.quests.*
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.ints.IntArrayList

val foods = CookingFood.values
val objs = CookingObj.values
val ingredients = CookingIngredient.values

val foodDefs = CookingFood.definitions
val cookedFoodDefs = CookingFood.values.associate { it.cooked_item to it }
val burntFoodDefs = CookingFood.values.associate { it.burnt_item to it }

val ingredientDefs = CookingIngredient.values.associate { it.result to it }

val cooking = Cooking(world.definitions)

val ingredientListenHashes = Int2ObjectOpenHashMap<Boolean>()
val objListenHashes = Int2ObjectOpenHashMap<Boolean>()

fun cookFoodOnRange(player: Player, item: Int, amount: Int = 28, obj: CookingObj?) {
    var def: CookingFood
    var forceBurn: Boolean
    if(burntFoodDefs[item] != null) {
        forceBurn = true
        def = burntFoodDefs[item] ?: return
    } else {
        forceBurn = false
        def = cookedFoodDefs[item] ?: return
    }

    player.queue(TaskPriority.STRONG) { cooking.cook(this, def, amount, obj!!, forceBurn = forceBurn) }
}

fun cookFoodOverFire(player: Player, item: Int, amount: Int = 28, obj: CookingObj?) {
    var def: CookingFood
    var forceBurn: Boolean

    if(cookedFoodDefs[item] != null) {
        forceBurn = true
        def = cookedFoodDefs[item] ?: return
    } else {
        forceBurn = false
        def = foodDefs[item] ?: return
    }
    player.queue(TaskPriority.STRONG) { cooking.cook(this,def,amount,obj!!, forceBurn = forceBurn) }
}

fun combineIngredients(player: Player, item: Int, amount: Int = 28, obj: CookingObj?) {
    val def = ingredientDefs[item] ?: return
    player.queue(TaskPriority.STRONG) { cooking.combine(this,def,amount) }
}

objs.forEach { obj ->
    if(obj.isRange) {
        on_obj_option(obj.objId, "cook") {
            val cookableFoods = findCookableFoods(player)
            player.queue(TaskPriority.WEAK) { cookingMessageBox(*cookableFoods, title = "What would you like to cook?", obj = obj, logic = ::cookFoodOnRange)}
        }
        on_obj_option(114, option = "cook") {
            player.queue {
                if (player.getVarp(29) < 1) {
                    chatNpc("Hey, who said you could use that?", 4626)
                } else {
                    val cookableFoods = findCookableFoods(player)
                    player.queue(TaskPriority.WEAK) { cookingMessageBox(*cookableFoods, title = "What would you like to cook?", obj = obj, logic = ::cookFoodOnRange)}
                }
            }
        }
    }

    foods.forEach { food ->
        on_item_on_obj(obj.objId, food.raw_item) {
            val foods = intArrayOf(food.raw_item)
            if(player.inventory.getItemCount(food.raw_item) == 1) {
                cookFoodOverFire(player, food.raw_item, 1, obj)
            } else {
                player.queue(TaskPriority.WEAK) { cookingMessageBox(*foods, title = "How many would you like to cook?", obj = obj, logic = ::cookFoodOverFire) }
            }
        }

        val max = Math.max(obj.objId, food.cooked_item)
        val min = Math.min(obj.objId, food.cooked_item)
        val hash = (max shl 16) or min

        if(!objListenHashes.containsKey(hash)) {
            on_item_on_obj(obj.objId, food.cooked_item) {
                val foods = intArrayOf(food.cooked_item)
                if(player.inventory.getItemCount(food.cooked_item) == 1) {
                    cookFoodOverFire(player, food.cooked_item, 1, obj)
                } else {
                    player.queue(TaskPriority.WEAK) { cookingMessageBox(*foods, title = "How many would you like to cook?", obj = obj, logic = ::cookFoodOverFire) }
                }
            }
            objListenHashes[hash] = true
        }

    }
}

ingredients.forEach { ingredient ->
    val max = Math.max(ingredient.item1, ingredient.item2)
    val min = Math.min(ingredient.item1, ingredient.item2)
    val hash = (max shl 16) or min

    if(!ingredientListenHashes.containsKey(hash)) {
        on_item_on_item(ingredient.item1, ingredient.item2) {
            val foods = findIngredientCombinations(player, ingredient)
            player.queue(TaskPriority.WEAK) { cookingMessageBox(*foods, title = "What do you wish to make?", obj = null, logic = ::combineIngredients) }
        }
        ingredientListenHashes[hash] = true
    }
}

fun findCookableFoods(player: Player): IntArray {
    val foods = IntArrayList()
    for(i in (0..27)) {
        val invItem = player.inventory.get(i)
        if(invItem != null) {
            if(cookedFoodDefs[invItem.id] != null) {
                if(!foods.contains(cookedFoodDefs[invItem.id]!!.burnt_item)) {
                    foods.add(cookedFoodDefs[invItem.id]!!.burnt_item)
                }
            }
        }
    }

    for(i in (0..27)) {
        val invItem = player.inventory.get(i)
        if (invItem != null) {
            if (foodDefs[invItem.id] != null) {
                if (!foods.contains(foodDefs[invItem.id]!!.cooked_item)) {
                    foods.add(foodDefs[invItem.id]!!.cooked_item)
                }
            }
        }
    }
    return foods.toIntArray()
}

fun findIngredientCombinations(player: Player, food: CookingIngredient): IntArray {
    val foods = IntArrayList()
    ingredients.forEach { ingredient ->
        if(food.item1 == ingredient.item1 && food.item2 == ingredient.item2 || food.item1 == ingredient.item2 && food.item2 == ingredient.item1) {
            foods.add(ingredient.result)
        }
    }

    return foods.toIntArray()
}