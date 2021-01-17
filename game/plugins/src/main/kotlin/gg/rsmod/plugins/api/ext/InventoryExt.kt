package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.attr.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.attr.OTHER_ITEM_SLOT_ATTR
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.api.Skills

/**
 * As the "inventory" itself is a specific [ItemContainer] of a [Player], extension functions
 * here are applied by way of the [Player] to allow for more context to these [Item] operations.
 *   Note| the functions provided herein are designed loosely "allowing" flexibility over slot
 *   awareness and even the container to use in operations and can be used in very unintended
 *   degrees, such as an [ItemContainer] which isn't the [Player]'s or maintaining presumed [getInteractingItemSlot]
 */

fun Player.maxPossible(vararg items: Int, container: ItemContainer = inventory): Int {
    val counts = mutableListOf<Int>()
    items.forEach { item ->
        counts.add(container.getItemCount(item))
    }
    return Math.min(counts.max()!!, container.capacity)
}

// not slot-aware (uses first available) defaults to inventory
fun Player.replaceItem(oldItem: Int, newItem: Int, container: ItemContainer = inventory): Boolean {
    return replaceItemInSlot(oldItem, newItem, -1, container)
}

// slot-aware defaulting to interacting slot and inventory
fun Player.replaceItemInSlot(oldItem: Int, newItem: Int, slot: Int = getInteractingItemSlot(), container: ItemContainer = inventory): Boolean {
    return container.replace(oldItem, newItem, slot)
}

/**
 * Works to replace all [oldItem] in [container] with [newItem] upto [max] times
 * in a loop with an increasing delay upto 3 cycles between each replacement and optional
 * slot awareness.
 */
fun Player.autoReplace(oldItem: Int, newItem: Int, growingDelay: Boolean = true, slotAware: Boolean = false,
                       container: ItemContainer = inventory, max: Int = container.getItemCount(oldItem), perform: () -> Unit, success: () -> Unit) {
    val count = container.getItemCount(oldItem)
    when {
        count <= 0 -> return
        count == 1 -> {
            perform()
            queue {
                when(slotAware){
                    false -> if(container.replace(oldItem, newItem)) success()
                    true -> if(container.replace(oldItem, newItem, container.getItemIndex(oldItem, false))) success()
                }
            }
        }
        else -> {
            var made = 0
            if(!slotAware) {
                queue {
                    while (container.contains(oldItem) && made < max){
                        if(growingDelay){
                            perform()
                            wait(Math.min(1 + made, 3))
                        } else {
                            perform()
                            wait(1)
                        }

                        if (container.replace(oldItem, newItem)){
                            made++
                            success()
                        } else
                            break
                    }
                }
            } else {
                var slot: Int = getInteractingItemSlot()
                queue {
                    while(container.contains(oldItem) && made < max){
                        if(growingDelay){
                            repeat(Math.min(1 + made, 2)){
                                perform()
                                wait(1)
                            }
                        } else {
                            perform()
                            wait(1)
                        }

                        if(container.replace(oldItem, newItem, slot)){
                            made++
                            success()
                            slot = container.getItemIndex(oldItem, false)
                        } else
                            break
                    }
                }
            }
        }
    }
}

fun Player.replaceItemWithSkillRequirement(oldItem: Int, newItem: Int,
        skill: Int, minLvl: Int = 1, slot: Int = -1,
        minLvlMessage: String = "You need level $minLvl ${Skills.getSkillName(world, skill)} to do that.",
        boostable: Boolean = true, container: ItemContainer = inventory): Boolean {
    val level = if(boostable) getSkills().getCurrentLevel(skill) else getSkills().getBaseLevel(skill)
    return if(level < minLvl){
        message(minLvlMessage)
        false
    } else
        container.replace(oldItem, newItem, slot)
}

/**
 * provides wrapper for [ItemContainer.replaceWithItemRequirement] with a customizable
 * message to respond with if [container] does not contain the [requiredItem]
 *   Note| this method is extremely loose and primarily for giving use to
 *   [ItemContainer.replaceWithItemRequirement] and adds customizable message feedback
 */
fun Player.replaceItemWithItemRequirement(oldItem: Int, newItem: Int, requiredItem: Int, slot: Int = -1,
        missingItemMessage: String = "You do not have the #ITEM needed to do that.",
        container: ItemContainer = inventory): Boolean {
    return if(!container.contains(requiredItem)){
        message(missingItemMessage.replaceItemName(requiredItem, world.definitions))
        false
    } else {
        // while item requirement has already been assured, no harm comes in using redundancy
        container.replaceWithItemRequirement(oldItem, newItem, requiredItem, slot)
    }
}

fun Player.replaceItemWithItemAndSkillRequirement(oldItem: Int, newItem: Int, requiredItem: Int,
        skill: Int, minLvl: Int = 1, slot: Int = -1,
        missingItemMessage: String = "You do not have the #ITEM needed to do that.",
        minLvlMessage: String = "You need level $minLvl ${Skills.getSkillName(world, skill)} to do that.",
        unboosted: Boolean = false, container: ItemContainer = inventory): Boolean {
    val level = if(!unboosted) getSkills().getCurrentLevel(skill) else getSkills().getBaseLevel(skill)
    return if(level < minLvl){
        message(minLvlMessage)
        false
    } else if(!container.contains(requiredItem)){
        message(missingItemMessage.replaceItemName(requiredItem, world.definitions))
        false
    } else
        container.replace(oldItem, newItem, slot)
}

// replace and remove another
fun Player.replaceItemAndRemoveAnother(oldItem: Int, newItem: Int, otherItem: Item, container: ItemContainer = inventory): Boolean {
    return container.replaceAndRemoveAnother(oldItem, newItem, otherItem, -1, -1)
}

fun Player.replaceItemAndRemoveAnotherInSlot(oldItem: Int, newItem: Int, otherItem: Item, slot: Int = -1, otherSlot: Int = -1, container: ItemContainer = inventory): Boolean {
    return container.replaceAndRemoveAnother(oldItem, newItem, otherItem, slot, otherSlot)
}

fun Player.replaceItemAndRemoveAnotherWithItemRequirement(oldItem: Int, newItem: Int, otherItem: Item, requiredItem: Int,
        slot: Int = -1, otherSlot: Int = -1,
        missingItemMessage: String = "You do not have the #ITEM needed to do that.",
        container: ItemContainer = inventory): Boolean {
    return if(!container.contains(requiredItem)){
        message(missingItemMessage.replaceItemName(requiredItem, world.definitions))
        false
    } else {
        // while item requirement has already been assured, no harm comes in using redundancy
        container.replaceAndRemoveAnotherWithItemRequirement(oldItem, newItem, otherItem, requiredItem, slot, otherSlot)
    }
}

fun Player.replaceItemAndRemoveAnotherWithSkillRequirement(oldItem: Int, newItem: Int, otherItem: Item,
        skill: Int, minLvl: Int = 1, slot: Int = -1, otherSlot: Int = -1,
        minLvlMessage: String = "You need level $minLvl ${Skills.getSkillName(world, skill)} to do that.",
        boostable: Boolean = true, container: ItemContainer = inventory): Boolean {
    val level = if(boostable) getSkills().getCurrentLevel(skill) else getSkills().getBaseLevel(skill)
    return if(level < minLvl){
        message(minLvlMessage)
        false
    } else {
        container.replaceAndRemoveAnother(oldItem, newItem, otherItem, slot, otherSlot)
    }
}

fun Player.replaceItemAndRemoveAnotherWithItemAndSkillRequirement(oldItem: Int, newItem: Int, otherItem: Item, requiredItem: Int,
        skill: Int, minLvl: Int = 1, slot: Int = -1, otherSlot: Int = -1,
        missingItemMessage: String = "You do not have the #ITEM needed to do that.",
        minLvlMessage: String = "You need level $minLvl ${Skills.getSkillName(world, skill)} to do that.",
        boostable: Boolean = true, container: ItemContainer = inventory): Boolean {
    val level = if(boostable) getSkills().getCurrentLevel(skill) else getSkills().getBaseLevel(skill)
    return if (level < minLvl) {
        message(minLvlMessage)
        false
    } else if(!container.contains(requiredItem)){
        message(missingItemMessage.replaceItemName(requiredItem, world.definitions))
        false
    } else {
        // while item requirement has already been assured, no harm comes in using redundancy
        container.replaceAndRemoveAnotherWithItemRequirement(oldItem, newItem, otherItem, requiredItem, slot, otherSlot)
    }
}

/**
 * if [slotAware] is true this method works to instill the first presented pair as the "main"
 * [Item] and slot determinations are derived from [INTERACTING_ITEM_SLOT] and [OTHER_ITEM_SLOT_ATTR]
 *   Note| if manual slot controls are needed instead, [container.replaceBoth] should be called directly
 */
fun Player.comboItemReplace(oldItem: Int, newItem: Int, otherOld: Int, otherNew: Int,
        slotAware: Boolean = false, container: ItemContainer = inventory): Boolean {
    return if(!slotAware)
        container.replaceBoth(oldItem, newItem, otherOld, otherNew)
    else {
        val interactSlot = getInteractingItemSlot()
        val mainSlot = if(inventory[interactSlot]!!.id == oldItem) interactSlot else attr[OTHER_ITEM_SLOT_ATTR]!!
        val otherSlot = if(mainSlot == interactSlot) attr[OTHER_ITEM_SLOT_ATTR]!! else interactSlot
        container.replaceBoth(oldItem, newItem, otherOld, otherNew, mainSlot, otherSlot)
    }
}

fun Player.produceItemBoxMessage(vararg itemsToMake: Int,
        title: String = if (itemsToMake.size==1) "How many do you wish to make?" else "What would you like to make?",
        max: Int = inventory.capacity, growingDelay: Boolean = false, logic: () -> Unit){
    when {
        max <= 0 -> {
            return
        }
        max == 1 -> {
            queue {
                logic()
            }
        }
        else -> {
            queue {
                produceItemBox(*itemsToMake, title = title, maxProducable = max){ _, qty ->
                    player.queue {
                        repeat(qty){
                            if(growingDelay) wait(Math.min(1+it,2)) else wait(1) // insures production tasks are not spammed
                            logic() // logic may contain it's own delays
                        }
                    }
                }
            }
        }
    }
}