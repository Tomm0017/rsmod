import gg.rsmod.plugins.osrs.api.ComponentPane
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.worldmap.WorldMap

onButton(parent = 160, child = 42) {
    val p = it.player()
    if (!p.lock.canComponentInteract()) {
        return@onButton
    }

    if (!p.isComponentVisible(WorldMap.COMPONENT_ID)) {
        val opt = it.getInteractingOption()
        p.sendWorldMapTile()
        p.openComponent(component = WorldMap.COMPONENT_ID, pane = ComponentPane.WORLD_MAP, fullscreen = opt == 1)
        if (opt == 1) {
            p.openComponent(component = WorldMap.FULLSCREEN_COMPONENT_ID, pane = ComponentPane.WORLD_MAP_FULL, fullscreen = true)
        }
        p.setComponentSetting(parent = WorldMap.COMPONENT_ID, child = 20, range = 0..4, setting = 2)
        p.timers[WorldMap.UPDATE_TIMER] = 1
    } else {
        p.closeComponent(WorldMap.COMPONENT_ID)
    }
}

onButton(parent = WorldMap.COMPONENT_ID, child = 37) {
    val p = it.player()
    p.closeComponent(WorldMap.COMPONENT_ID)
    p.sendDisplayComponent(p.components.displayMode)
    p.attr.remove(WorldMap.LAST_TILE)
    p.timers.remove(WorldMap.UPDATE_TIMER)
}

onTimer(WorldMap.UPDATE_TIMER) {
    val p = it.player()

    if (p.isComponentVisible(WorldMap.COMPONENT_ID)) {
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