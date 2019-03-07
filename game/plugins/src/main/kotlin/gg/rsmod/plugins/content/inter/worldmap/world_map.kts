package gg.rsmod.plugins.content.inter.worldmap

on_button(interfaceId = 160, component = 42) {
    val p = player
    if (!p.lock.canInterfaceInteract()) {
        return@on_button
    }

    if (!p.isInterfaceVisible(WorldMap.INTERFACE_ID)) {
        val opt = getInteractingOption()
        p.sendWorldMapTile()
        p.openInterface(interfaceId = WorldMap.INTERFACE_ID, dest = InterfaceDestination.WORLD_MAP, fullscreen = opt == 2)
        if (opt == 2) {
            p.openInterface(interfaceId = WorldMap.FULLSCREEN_INTERFACE_ID, dest = InterfaceDestination.WORLD_MAP_FULL, fullscreen = true)
        }
        p.setInterfaceEvents(interfaceId = WorldMap.INTERFACE_ID, component = 20, range = 0..4, setting = 2)
        p.timers[WorldMap.UPDATE_TIMER] = 1
    } else {
        p.closeInterface(WorldMap.INTERFACE_ID)
    }
}

on_button(interfaceId = WorldMap.INTERFACE_ID, component = 37) {
    val p = player
    p.closeInterface(WorldMap.INTERFACE_ID)
    p.openOverlayInterface(p.interfaces.displayMode)
    p.attr.remove(WorldMap.LAST_TILE)
    p.timers.remove(WorldMap.UPDATE_TIMER)
}

on_timer(WorldMap.UPDATE_TIMER) {
    val p = player

    if (p.isInterfaceVisible(WorldMap.INTERFACE_ID)) {
        /**
         * Only send the world when the last tile recorded is not the same as
         * the current one being stood on, so we're not needlessly sending the
         * script every cycle.
         */
        val lastTile = p.attr[WorldMap.LAST_TILE]
        if (lastTile == null || !lastTile.sameAs(p.tile)) {
            p.sendWorldMapTile()
            p.attr[WorldMap.LAST_TILE] = p.tile
        }

        p.timers[WorldMap.UPDATE_TIMER] = 1
    }
}