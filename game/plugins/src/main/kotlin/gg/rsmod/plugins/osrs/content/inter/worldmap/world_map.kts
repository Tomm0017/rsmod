import gg.rsmod.plugins.osrs.api.InterfacePane
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.worldmap.WorldMap

r.bindButton(parent = 160, child = 42) {
    val p = it.player()
    if (!p.lock.canInterfaceInteract()) {
        return@bindButton
    }

    if (!p.isInterfaceVisible(WorldMap.INTERFACE_ID)) {
        val opt = it.getInteractingOption()
        p.sendWorldMapTile()
        p.openInterface(interfaceId = WorldMap.INTERFACE_ID, pane = InterfacePane.WORLD_MAP, fullscreen = opt == 1)
        if (opt == 1) {
            p.openInterface(interfaceId = WorldMap.FULLSCREEN_INTERFACE_ID, pane = InterfacePane.WORLD_MAP_FULL, fullscreen = true)
        }
        p.setInterfaceSetting(parent = WorldMap.INTERFACE_ID, child = 20, range = 0..4, setting = 2)
        p.timers[WorldMap.UPDATE_TIMER] = 1
    } else {
        p.closeInterface(WorldMap.INTERFACE_ID)
    }
}

r.bindButton(parent = WorldMap.INTERFACE_ID, child = 37) {
    it.player().closeInterface(WorldMap.INTERFACE_ID)
    it.player().sendDisplayInterface(it.player().interfaces.displayMode)
}

r.bindTimer(WorldMap.UPDATE_TIMER) {
    val p = it.player()

    if (p.isInterfaceVisible(WorldMap.INTERFACE_ID)) {
        /**
         * Only send the world map so we're not needlessly sending the script
         * every cycle.
         */
        val lastTile = p.attr[WorldMap.LAST_TILE]
        if (lastTile == null || !lastTile.sameAs(p.tile)) {
            p.sendWorldMapTile()
            p.attr[WorldMap.LAST_TILE] = p.tile
        }

        p.timers[WorldMap.UPDATE_TIMER] = 1
    }
}