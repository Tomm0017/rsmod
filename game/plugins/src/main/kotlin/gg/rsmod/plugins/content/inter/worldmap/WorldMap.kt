package gg.rsmod.plugins.content.inter.worldmap

import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.timer.TimerKey

/**
 * @author Tom <rspsmods@gmail.com>
 */
object WorldMap {

    val UPDATE_TIMER = TimerKey()
    val LAST_TILE = AttributeKey<Tile>()

    const val WORLD_MAP_INTERFACE_ID = 595
    const val WORLD_MAP_FULLSCREEN_INTERFACE_ID = 594
}