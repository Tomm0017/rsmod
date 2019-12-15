package gg.rsmod.plugins.content.mechanics.ships


/**
 * Loops through CaptainsList, -> checks on_npc_option,
 * if ( Pay-Fare/Take-boat  ->  useShip)
 * if ( Talk-to -> initiates dialogue with captain)
 */
Captains.CAPTAINS.forEach { captain ->
    if (world.definitions.get(NpcDef::class.java, captain.captainId).options.contains("Pay-Fare")) {
        on_npc_option(captain.captainId, "Pay-Fare") {
            captain.tripType.forEach { trip ->
                if (player.tile.regionId == trip.boardingRegion) {
                    player.queue(TaskPriority.STRONG) { this.useShip(trip) }
                }
            }
        }
    }
    if (world.definitions.get(NpcDef::class.java, captain.captainId).options.contains("Take-boat")) {
        on_npc_option(captain.captainId, "Take-boat") {
            captain.tripType.forEach { trip ->
                if (player.tile.regionId == trip.boardingRegion) {
                    player.queue(TaskPriority.STRONG) { this.useShip(trip) }
                }
            }
        }
    }
    if (world.definitions.get(NpcDef::class.java, captain.captainId).options.contains("Talk-to")) {
        on_npc_option(captain.captainId, "Talk-to") {
            captain.tripType.forEach { trip ->
                if (player.tile.regionId == trip.boardingRegion) {
                    player.queue(TaskPriority.STRONG) {
                        chatNpc("Do you want to go on a trip to ${trip.destination}?")
                        chatNpc("This trip will cost you ${trip.sailCost} coins.")
                        when (options("Yes please.", "No, thank you.")) {
                            1 -> {
                                chatPlayer("Yes please.")
                                this.useShip(trip)
                            }
                            2 -> {
                                chatPlayer("No, thank you.")
                            }
                        }
                    }
                }
            }
        }
    }
}
/**
 * Function useShip
 * checks if (player has sail cost)  {true => (take fund -> lock player -> disableTabs ->)
 *
 */
suspend fun QueueTask.useShip(trip : TripType) {
    if (player.inventory.getItemCount(itemId = Items.COINS_995) >= trip.sailCost) {
        player.setComponentText(interfaceId = 299, component = 25, text = "You sail to ${trip.destination}")
        player.lock = LockState.DELAY_ACTIONS
        player.inventory.remove(item = 995, amount = trip.sailCost)
        SailingInterface.disableTabs(player)
        player.moveTo(tile = Tile(4600, 1893))
        player.setVarp(id = 75, value = trip.varp)
        player.openInterface(interfaceId = 299, dest = InterfaceDestination.MAIN_SCREEN)
        player.message("You pay ${trip.sailCost} coins and board a ship.")
        wait(cycles = trip.varpDelay)
        player.unlock()
        player.moveTo(trip.dstTile)
        player.setVarp(id = 75, value = 0)
        player.closeInterface(interfaceId = 299)
        SailingInterface.enableTabs(player)
        messageBox("The ship arrives at ${trip.destination}.")
    } else {
        player.message("You do not have enough money for that.")
    }
}