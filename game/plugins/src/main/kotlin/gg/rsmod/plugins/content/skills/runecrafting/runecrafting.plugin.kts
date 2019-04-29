package gg.rsmod.plugins.content.skills.runecrafting

private val enterOption = "Enter"

Altar.values.forEach { altar ->

    /**
     * Handle each Mysterious Ruins object for the Runecrafting altar
     */
    altar.ruins?.forEach { ruin ->

        // The object definition for the Mysterious Ruins
        val def = world.definitions.get(ObjectDef::class.java, ruin)

        // Allow using the talisman on the ruins to enter the altar
        altar.talisman?.let { talisman ->
            on_item_on_obj(obj = ruin, item = talisman) {
                altar.entrance?.let { player.moveTo(it) }
            }
        }

        // If the object has the 'enter' option, we should check that the varbit is set for the player before teleporting them to the altar
        if (def.options.contains(enterOption)) {
            on_obj_option(obj = ruin, option = enterOption) {
                if (player.getVarbit(altar.varbit) == 1) {
                    altar.entrance?.let { player.moveTo(it) }
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
            player.moveTo(altar.exit)
        }
    }

    /**
     * Handle the locate option on a talisman
     */
    altar.talisman?.let {
        on_item_option(item = it, option = "locate") {

            // The tile of the ruins
            val tile = altar.exit!!
            val pos = player.tile

            // The direction of the altar
            val direction : String = when {
                pos.z > tile.z && pos.x - 1 > tile.x -> "south-west"
                pos.x < tile.x && pos.z > tile.z -> "south-east"
                pos.x > tile.x + 1 && pos.z < tile.z -> "north-west"
                pos.x < tile.x && pos.z < tile.z -> "north-east"
                pos.z < tile.z -> "north"
                pos.z > tile.z -> "south"
                pos.x < tile.x + 1 -> "east"
                pos.x > tile.x + 1 -> "west"
                else -> "unknown"
            }

            player.message("The talisman pulls towards the $direction.")
        }
    }
}