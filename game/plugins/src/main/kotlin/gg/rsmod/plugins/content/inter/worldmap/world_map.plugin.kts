package gg.rsmod.plugins.content.inter.worldmap

on_button(interfaceId = 160, component = 43) {
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }

    if (!player.isInterfaceVisible(WorldMap.INTERFACE_ID)) {
        val opt = player.getInteractingOption()
        player.sendWorldMapTile()
        player.openInterface(interfaceId = WorldMap.INTERFACE_ID, dest = InterfaceDestination.WORLD_MAP, fullscreen = opt == 2)
        if (opt == 2) {
            player.openInterface(interfaceId = WorldMap.FULLSCREEN_INTERFACE_ID, dest = InterfaceDestination.WORLD_MAP_FULL, fullscreen = true)
        }
        player.setInterfaceEvents(interfaceId = WorldMap.INTERFACE_ID, component = 20, range = 0..4, setting = 2)
        player.timers[WorldMap.UPDATE_TIMER] = 1
    } else {
        player.closeInterface(WorldMap.INTERFACE_ID)
    }
}

on_button(interfaceId = WorldMap.INTERFACE_ID, component = 37) {
    player.closeInterface(WorldMap.INTERFACE_ID)
    player.openOverlayInterface(player.interfaces.displayMode)
    player.attr.remove(WorldMap.LAST_TILE)
    player.timers.remove(WorldMap.UPDATE_TIMER)
}

on_timer(WorldMap.UPDATE_TIMER) {
    if (player.isInterfaceVisible(WorldMap.INTERFACE_ID)) {
        /*
         * Only send the world when the last tile recorded is not the same as
         * the current one being stood on, so we're not needlessly sending the
         * script every cycle.
         */
        val lastTile = player.attr[WorldMap.LAST_TILE]
        if (lastTile == null || !lastTile.sameAs(player.tile)) {
            player.sendWorldMapTile()
            player.attr[WorldMap.LAST_TILE] = player.tile
        }

        player.timers[WorldMap.UPDATE_TIMER] = 1
    }
}