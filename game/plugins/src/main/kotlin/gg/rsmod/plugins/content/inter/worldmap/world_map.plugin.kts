package gg.rsmod.plugins.content.inter.worldmap

import gg.rsmod.plugins.content.inter.worldmap.WorldMap.WORLD_MAP_INTERFACE_ID
import gg.rsmod.plugins.content.inter.worldmap.WorldMap.WORLD_MAP_FULLSCREEN_INTERFACE_ID
import gg.rsmod.plugins.content.inter.worldmap.WorldMap.LAST_TILE
import gg.rsmod.plugins.content.inter.worldmap.WorldMap.UPDATE_TIMER

on_button(interfaceId = 160, component = 46) {
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }

    if (!player.isInterfaceVisible(WORLD_MAP_INTERFACE_ID)) {
        val opt = player.getInteractingOption()
        player.sendWorldMapTile()
        player.openInterface(interfaceId = WORLD_MAP_INTERFACE_ID, dest = InterfaceDestination.WORLD_MAP, fullscreen = opt != 2)
        if (opt != 2) {
            player.openInterface(interfaceId = WORLD_MAP_FULLSCREEN_INTERFACE_ID, dest = InterfaceDestination.WORLD_MAP_FULL, fullscreen = true)
        }
        player.setInterfaceEvents(interfaceId = WORLD_MAP_INTERFACE_ID, component = 20, range = 0..4, setting = 2)
        player.timers[UPDATE_TIMER] = 1
    } else {
        player.closeInterface(WORLD_MAP_INTERFACE_ID)
    }
}

/**
 * Esc key closes.
 */
on_button(interfaceId = WORLD_MAP_INTERFACE_ID, component = 4) {
    player.closeInterface(WORLD_MAP_INTERFACE_ID)
    player.openOverlayInterface(player.interfaces.displayMode)
    player.attr.remove(LAST_TILE)
    player.timers.remove(UPDATE_TIMER)
}

/**
 * 'x' button closes
 */
on_button(interfaceId = WORLD_MAP_INTERFACE_ID, component = 38) {
    player.closeInterface(WORLD_MAP_INTERFACE_ID)
    player.openOverlayInterface(player.interfaces.displayMode)
    player.attr.remove(LAST_TILE)
    player.timers.remove(UPDATE_TIMER)
}

on_timer(UPDATE_TIMER) {
    if (player.isInterfaceVisible(WORLD_MAP_INTERFACE_ID)) {
        /*
         * Only send the world when the last tile recorded is not the same as
         * the current one being stood on, so we're not needlessly sending the
         * script every cycle.
         */
        val lastTile = player.attr[LAST_TILE]
        if (lastTile == null || !lastTile.sameAs(player.tile)) {
            player.sendWorldMapTile()
            player.attr[LAST_TILE] = player.tile
        }

        player.timers[UPDATE_TIMER] = 1
    }
}