package gg.rsmod.plugins.content.areas.varrock.objs

import net.runelite.http.api.item.ItemType

val SLASH_WEP = 2500
val SLASH_KNIFE = 2548

val SLASH = intArrayOf(
        Items.BRONZE_SCIMITAR, Items.IRON_SCIMITAR, Items.STEEL_SCIMITAR,
        Items.MITHRIL_SCIMITAR, Items.ADAMANT_SCIMITAR, Items.RUNE_SCIMITAR
)

on_obj_option(obj = Objs.WEB_733, option = "slash") {
    slash(player, player.getInteractingGameObj())
}

on_item_on_obj(Objs.WEB_733, Items.KNIFE, 1) {
    when (world.random(1)) {
        0 -> {
            val obj = player.getInteractingGameObj()
            player.lock = LockState.DELAY_ACTIONS
            player.playSound(SLASH_KNIFE)
            player.animate(911)
            player.message("You slash the web apart.")
            player.unlock()
            world.queue {
                wait(3)
                world.remove(obj)
                world.spawn(DynamicObject(obj, Objs.SLASHED_WEB))
                wait(96)
                world.remove(DynamicObject(obj, Objs.SLASHED_WEB))
                world.spawn(DynamicObject(obj))
            }
        }
        1 -> {
            player.lock = LockState.DELAY_ACTIONS
            player.playSound(SLASH_KNIFE)
            player.animate(911)
            player.message("You fail to cut through it.")
            player.unlock()
        }
    }
}

fun slash(p: Player, obj: GameObject) {
    when (world.random(1)) {
        0 -> {
            if (p.hasEquipped(EquipmentType.WEAPON, *SLASH)) {
                p.lock = LockState.DELAY_ACTIONS
                p.playSound(SLASH_WEP)
                p.animate(390)
                p.message("You slash the web apart.")
                p.unlock()
                world.queue {
                    wait(3)
                    world.remove(obj)
                    world.spawn(DynamicObject(obj, Objs.SLASHED_WEB))
                    wait(96)
                    world.remove(DynamicObject(obj, Objs.SLASHED_WEB))
                    world.spawn(DynamicObject(obj))
                }
            } else if (p.inventory.contains(946)) {
                p.lock = LockState.DELAY_ACTIONS
                p.playSound(SLASH_KNIFE)
                p.animate(911)
                p.message("You slash the web apart.")
                p.unlock()
                world.queue {
                    wait(3)
                    world.remove(obj)
                    world.spawn(DynamicObject(obj, Objs.SLASHED_WEB))
                    wait(96)
                    world.remove(DynamicObject(obj, Objs.SLASHED_WEB))
                    world.spawn(DynamicObject(obj))
                }
            } else p.message("Only a sharp blade can cut through this sticky web.")
        }
        1 -> {
            if (p.hasEquipped(EquipmentType.WEAPON, *SLASH)) {
                p.lock = LockState.DELAY_ACTIONS
                p.playSound(SLASH_WEP)
                p.animate(390)
                p.message("You fail to cut through it.")
                p.unlock()
            } else if (p.inventory.contains(946)) {
                p.lock = LockState.DELAY_ACTIONS
                p.playSound(SLASH_KNIFE)
                p.animate(911)
                p.message("You fail to cut through it.")
                p.unlock()
            } else p.message("Only a sharp blade can cut through this sticky web.")
        }
    }
}