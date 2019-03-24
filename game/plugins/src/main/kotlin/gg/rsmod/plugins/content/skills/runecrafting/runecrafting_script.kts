package gg.rsmod.plugins.content.skills.runecrafting

import gg.rsmod.game.fs.def.ObjectDef

private val enterOption = "Enter"

Altar.values.forEach { altar ->

    /**
     * Handle each Mysterious Ruins object for the Runecrafting altar
     */
    altar.ruins?.forEach { ruin ->

        // The object definition for the Mysterious Ruins
        val def = world.definitions.get(ObjectDef::class.java, ruin)

        // If the object has the 'enter' option, we should check that the varbit is set for the player before teleporting them to the altar
        if (def.options.contains(enterOption)) {
            on_obj_option(obj = ruin, option = enterOption) {
                if (player.getVarbit(altar.varbit) == 1) {
                    altar.entrance?.let { player.teleport(it) }
                }
            }
        }
    }

    /**
     * Handle the enabling of the Mysterious Ruins varbit when equipping
     * the respective tiara
     */
    if (altar.tiara != null) {
        on_item_equip(item = altar.tiara) {
            player.setVarbit(altar.varbit, 1)
        }
    }

    /**
     * Handle the disabling of the Mysterious Ruins varbit when removing
     * the respective tiara
     */
    if (altar.tiara != null) {
        on_item_unequip(item = altar.tiara) {
            player.setVarbit(altar.varbit, 0)
        }
    }

    /**
     * Handle the crafting action
     */
    on_obj_option(obj = altar.altar, option = altar.option) {
        player.queue {
            RunecraftAction.craftRune(this, altar.rune)
        }
    }

    /**
     * Handle the exit portal for the altar
     */
    if (altar.exitPortal != null && altar.exit != null) {
        on_obj_option(obj = altar.exitPortal, option = "use") {
            player.teleport(altar.exit)
        }
    }
}