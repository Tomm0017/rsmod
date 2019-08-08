package gg.rsmod.plugins.content.skills.cooking

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.*
/*import gg.rsmod.plugins.content.modules.tutorialisland.TutorialIsland
import gg.rsmod.plugins.content.modules.tutorialisland.events.CookedBreadEvent
import gg.rsmod.plugins.content.modules.tutorialisland.events.CookedShrimpEvent
import gg.rsmod.plugins.content.modules.tutorialisland.events.CreateBreadDoughEvent*/
import gg.rsmod.plugins.content.skills.cooking.data.CookingFood
import gg.rsmod.plugins.content.skills.cooking.data.CookingIngredient
import gg.rsmod.plugins.content.skills.cooking.data.CookingObj

class Cooking(private val defs: DefinitionSet) {

    val foodNames = CookingFood.values.associate { it.raw_item to defs.get(ItemDef::class.java, it.raw_item).name.toLowerCase() }
    val cookedFoodNames = CookingFood.values.associate { it.cooked_item to defs.get(ItemDef::class.java, it.cooked_item).name.toLowerCase() }

    val ingredientNames = CookingIngredient.values.associate { it.result to defs.get(ItemDef::class.java, it.result).name.toLowerCase() }

    suspend fun cook(task: QueueTask, food: CookingFood, amount: Int, obj: CookingObj, forceBurn: Boolean = false) {
        val player = task.player

        val name = foodNames[food.raw_item] ?: return
        val burnName = cookedFoodNames[food.cooked_item] ?: return
        var cookable: Int

        if(forceBurn) {
            cookable = food.cooked_item
        } else {
            cookable = food.raw_item
        }

        repeat(amount) {

            if(!canCook(player, food, forceBurn)) {
                return
            }

            player.animate(obj.animation)
            player.playSound(obj.sound, 1, 0)

            player.inventory.remove(cookable)
            val level = player.getSkills().getCurrentLevel(Skills.COOKING)
            if(forceBurn) {
                player.inventory.add(food.burnt_item)
                player.filterableMessage("You deliberately burn some ${burnName}.")
            }
            else if(level.interpolate(minChance = 60, maxChance = 190, minLvl = food.minLevel, maxLvl = food.maxLevel, cap = 255)) /*|| player.getVarp(TutorialIsland.COMPLETION_VARP) == 90 || player.getVarp(TutorialIsland.COMPLETION_VARP) == 160)*/ {
                player.inventory.add(food.cooked_item, 1)
                player.addXp(Skills.COOKING, food.xp)
                player.filterableMessage("You cook some ${name}.")
                /*if(player.getVarp(TutorialIsland.COMPLETION_VARP) == 90) {
                    player.world.plugins.executeEvent(player, CookedShrimpEvent)
                } else if(player.getVarp(TutorialIsland.COMPLETION_VARP) == 160) {
                  player.world.plugins.executeEvent(player, CookedBreadEvent)
                } else {
                    player.filterableMessage("You cook some ${name}.")
                }*/

            } else {
                player.inventory.add(food.burnt_item, 1)
                player.filterableMessage("You accidentally burn some ${name}.")
            }
            task.wait(5)
            player.animate(-1)
        }
    }

    private fun canCook(player: Player, food: CookingFood, forceBurn: Boolean): Boolean {
        var name: String
        var cookable: Int
        if(forceBurn) {
            name = cookedFoodNames[food.cooked_item] ?: return false
            cookable = food.cooked_item
        } else {
            name = foodNames[food.raw_item] ?: return false
            cookable = food.raw_item
        }

        if(!player.inventory.contains(cookable)) {
            if(!forceBurn) {
                player.filterableMessage("You don't have any more $name to cook.")
            } else {
                player.filterableMessage("You don't have any more $name to burn.")
            }
            return false
        }

        if(player.getSkills().getCurrentLevel(Skills.COOKING) < food.minLevel && !forceBurn) {
            player.filterableMessage("You need a ${Skills.getSkillName(player.world, Skills.COOKING)} level of at least ${food.minLevel} to cook ${name}.")
            return false
        }
        return true
    }

    suspend fun combine(task: QueueTask, ingredient: CookingIngredient, amount: Int) {
        val player = task.player

        val name = ingredientNames[ingredient.result] ?: return

        repeat(amount) {

            if(!canCombine(player, ingredient)) {
                return
            }

            player.inventory.remove(ingredient.item1)
            player.inventory.remove(ingredient.item2)

            if(ingredient.usedItem1 != -1) {
                player.inventory.add(ingredient.usedItem1,1)
            }
            if(ingredient.usedItem2 != -1) {
                player.inventory.add(ingredient.usedItem2, 1)
            }

            player.inventory.add(ingredient.result, 1)
            if(ingredient.xp > 0.0) {
                player.addXp(Skills.COOKING, ingredient.xp)
            }

            /*if(player.getVarp(TutorialIsland.COMPLETION_VARP) == 150) {
                player.world.plugins.executeEvent(player, CreateBreadDoughEvent)
            } else {
                player.filterableMessage("You combine the ingredients to make $name.")
            }*/
            player.filterableMessage("You combine the ingredients to make $name.")
            task.wait(cycles = 3)
        }
    }


    private fun canCombine(player: Player, ingredient: CookingIngredient): Boolean {
        val name = ingredientNames[ingredient.result] ?: return false
        if(!player.inventory.contains(ingredient.item1) || !player.inventory.contains(ingredient.item2)) {
            player.filterableMessage("You are missing the required ingredients to make $name.")
            /*if(player.getVarp(TutorialIsland.COMPLETION_VARP) >= 1000) {
                player.filterableMessage("You are missing the required ingredients to make $name.")
            }*/
            return false
        }

        if(player.getSkills().getCurrentLevel(Skills.COOKING) < ingredient.minLevel) {
            player.filterableMessage("You must have a ${Skills.getSkillName(player.world, Skills.COOKING)} level of ${ingredient.minLevel} to make $name.")
            return false
        }

        if(player.inventory.freeSlotCount == 0) {
            player.filterableMessage("You don't have enough inventory space to make $name.")
            return false
        }
        return true
    }
}